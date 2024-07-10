package com.consultorio.casos_judiciales.services;

import com.consultorio.casos_judiciales.models.Usuario;
import com.consultorio.casos_judiciales.repositories.UsuarioRepository;
import com.consultorio.casos_judiciales.utils.UserRole;
import com.consultorio.casos_judiciales.utils.Status;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Usuario saveUSer(Usuario usuario, UserRole role) throws BadRequestException {
        String email = usuario.getEmail();

        if (isEmailInUse(email)){
            throw new BadRequestException("The email address: '"+email.toUpperCase()+"' is already in use");
        }

        Usuario u = Usuario.builder()
                .email(usuario.getEmail().toLowerCase())
                .name(usuario.getName().toLowerCase())
                .lastname(usuario.getLastname().toLowerCase())
                .role(role)
                .status(Status.ACTIVE)
                .password(passwordEncoder.encode(usuario.getPassword()))
                .build();

        return usuarioRepository.save(u);
    }

    public Usuario createUserAsClient(Usuario usuario) throws BadRequestException {
        return saveUSer(usuario, UserRole.CLIENTE);
    }

    public Usuario createUserAsLawyer(Usuario usuario) throws BadRequestException {
        return saveUSer(usuario, UserRole.ABOGADO);

    }
    public Usuario createUserAsAdmin(Usuario usuario) throws BadRequestException {
        return saveUSer(usuario, UserRole.ADMIN);
    }

    private boolean isEmailInUse(String email){
        return usuarioRepository.findByEmail(email).isPresent();
    }

    /**public Optional<Usuarios> findUserById(String id){
        return usuarioRepository.findById(id);
    }**/

    private String getUserRole(String id){
        if (findUSerByIDActive(id).isPresent()){
            return findUSerByIDActive(id).get().getRole().toString();
        }
        else {
            return "User not found";
        }
    }
    public boolean isUserALawyer(String id){
        return getUserRole(id).equals("ABOGADO");
    }
    public boolean isUserAClient(String id){
        return getUserRole(id).equals("CLIENTE");
    }
    public Optional<Usuario>findUSerByIDActive(String id){
        return usuarioRepository.findByuuidAndStatus(id, Status.ACTIVE);
    }
}
