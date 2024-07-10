package com.consultorio.casos_judiciales.config;


import com.consultorio.casos_judiciales.models.Usuario;
import com.consultorio.casos_judiciales.repositories.UsuarioRepository;
import com.consultorio.casos_judiciales.services.JwtService;
import com.consultorio.casos_judiciales.utils.Status;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        //filterChain.doFilter(request, response);

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);

        if (jwtService.isTokenExpired(jwt)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token was expired");
            return;
        }

        if (!jwtService.isTokenValid(jwt)){
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token not valid");
            return;
        }

        String email = jwtService.getEmailFromToken(jwt);

        Optional<Usuario> users = usuarioRepository.findByEmailAndStatus(email, Status.ACTIVE);

        if ( users.isPresent() && jwtService.isTokenValid( jwt ) ){
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken( email, null, users.get().getAuthorities() );
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request,response);
        }

        filterChain.doFilter(request,response);
    }

}
