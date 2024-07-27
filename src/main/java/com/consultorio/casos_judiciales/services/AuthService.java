package com.consultorio.casos_judiciales.services;

import com.consultorio.casos_judiciales.dtos.request.SignInRequest;
import com.consultorio.casos_judiciales.dtos.response.SignInResponse;
import com.consultorio.casos_judiciales.dtos.response.ValidateTokenResponse;
import com.consultorio.casos_judiciales.models.Usuarios;
import com.consultorio.casos_judiciales.repositories.UsuariosRepository;
import com.consultorio.casos_judiciales.utils.Status;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsuariosRepository usuariosRepository;
    @Autowired
    private UsuarioService usuarioService;

    public SignInResponse signIn(SignInRequest request){
        String e = request.getEmail().toLowerCase();
        String p = request.getPassword().toLowerCase();

        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(e, p));

        Optional<Usuarios> users = usuariosRepository.findByEmailAndStatus(e, Status.ACTIVE);

        if (users.isEmpty()){
            throw  new UsernameNotFoundException("User not found");
        }
        String token = jwtService.generateToken(users.get());

        return SignInResponse.builder()
                .token(token)
                .usuario(users.get())
                .build();
    }
    public  boolean isValidToken(String token){
        return jwtService.isTokenValid(token);
    }

    public ValidateTokenResponse validateToken(String token) {

        String id = jwtService.getIDFromToken(token);
        Usuarios usuario = usuarioService.findUSerById(id).orElse(null);

        String nuevoToken = jwtService.generateToken(usuario);
        return new ValidateTokenResponse(usuario, nuevoToken);
    }

}
