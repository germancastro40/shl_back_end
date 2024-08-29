package com.consultorio.casos_judiciales.services;

import com.consultorio.casos_judiciales.dtos.request.NewPasswordRequest;
import com.consultorio.casos_judiciales.dtos.request.SignInRequest;
import com.consultorio.casos_judiciales.dtos.response.SignInResponse;
import com.consultorio.casos_judiciales.dtos.response.ValidateTokenResponse;
import com.consultorio.casos_judiciales.enums.Status;
import com.consultorio.casos_judiciales.models.UsersCodes;
import com.consultorio.casos_judiciales.models.Usuarios;
import com.consultorio.casos_judiciales.repositories.UsuariosRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersCodeService usersCodeService;

    public SignInResponse signIn(SignInRequest request){
        String e = request.getEmail().toLowerCase();
        String p = request.getPassword();

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

    public String activateUser(NewPasswordRequest request) throws BadRequestException {
        UsersCodes code = validateAndRetrieveCode(request.getCode(), usersCodeService::findCodeToActivateUser);
        updatePassword(code.getUsuarios(), request.getNewPassword());
        usersCodeService.invalidateCode(code);
        return "User activated successfully";
    }

    public String changeUsersPassword(NewPasswordRequest request) throws BadRequestException {
        UsersCodes code = validateAndRetrieveCode(request.getCode(), usersCodeService::findCodeToResetPassword);
        updatePassword(code.getUsuarios(), request.getNewPassword());
        usersCodeService.invalidateCode(code);
        return "Password updated successfully";
    }

    private UsersCodes validateAndRetrieveCode(String code, Function<String, Optional<UsersCodes>> codeFinder) throws BadRequestException {
        UsersCodes usersCode = codeFinder.apply(code)
                .orElseThrow(() -> new BadRequestException("The code provided isn't valid"));
        if (new Date().after(usersCode.getExpiredAt())) {
            throw new BadRequestException("The code has expired");
        }
        return usersCode;
    }

    private void updatePassword(Usuarios usuario, String newPassword) {
        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuariosRepository.save(usuario);
    }


}
