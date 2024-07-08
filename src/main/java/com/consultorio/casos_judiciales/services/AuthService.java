package com.consultorio.casos_judiciales.services;

import com.consultorio.casos_judiciales.dtos.request.SignInRequest;
import com.consultorio.casos_judiciales.dtos.response.SignInResponse;
import com.consultorio.casos_judiciales.models.Usuarios;
import com.consultorio.casos_judiciales.repositories.UsuarioRepository;
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
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioService usuarioService;

    public SignInResponse signIn(SignInRequest request){
        String e = request.getEmail();
        String p = request.getPassword();

        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(e, p));

        Optional<Usuarios> users = usuarioRepository.findByEmailAndStatus(e, Status.ACTIVE);

        if (users.isEmpty()){
            throw  new UsernameNotFoundException("User not found");
        }
        String token = jwtService.generateToken(users.get());

        return SignInResponse.builder()
                .token(token)
                .usuarios(users.get())
                .build();
    }

}
