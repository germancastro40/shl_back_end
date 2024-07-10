package com.consultorio.casos_judiciales.services;

import com.consultorio.casos_judiciales.models.Usuario;
import com.consultorio.casos_judiciales.repositories.UsuarioRepository;
import com.consultorio.casos_judiciales.utils.Status;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
@NoArgsConstructor
public class JwtService {
    @Value("${signature.jwt.key}")
    private String secretKey;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public String generateToken(Usuario usuario){
        return Jwts
                .builder()
                .setClaims(generateExtraClaims(usuario))
                .setSubject(usuario.getUuid())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    private Map<String, Object>generateExtraClaims(Usuario usuario){
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put( "email", usuario.getEmail() );
        extraClaims.put( "role", usuario.getRole() );
        return extraClaims;
    }

    private Key getKey(){
        byte[] bytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String getIDFromToken(String token){
        return getClaims(token, Claims::getSubject);
    }

    public String getEmailFromToken(String token){
        Claims claims = getAllClaims(token);
        return claims.get("email").toString();
    }

    private Claims getAllClaims(String token){
        return  Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaims(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getDateExpiration(String token) {
        try {
            return getClaims(token, Claims::getExpiration);
        } catch (ExpiredJwtException e) {
            return new Date(0);
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            return getDateExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public boolean isTokenValid(String token){
        String email = getEmailFromToken(token);
        Optional<Usuario> users = usuarioRepository.findByEmailAndStatus(email, Status.ACTIVE);

        return (users.isPresent() && email.equals(users.get().getEmail()) && !isTokenExpired(token));
    }
}
