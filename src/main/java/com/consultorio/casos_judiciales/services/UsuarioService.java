package com.consultorio.casos_judiciales.services;

import com.consultorio.casos_judiciales.models.Usuarios;
import com.consultorio.casos_judiciales.repositories.UsuariosRepository;
import com.consultorio.casos_judiciales.utils.Status;
import com.consultorio.casos_judiciales.utils.UserRole;
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
    private UsuariosRepository usuariosRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Usuarios saveUSer(Usuarios usuario, UserRole role) throws BadRequestException {
        String email = usuario.getEmail();

        if (isEmailInUse(email)){
            throw new BadRequestException("The email address: '"+email.toUpperCase()+"' is already in use");
        }

        Usuarios u = Usuarios.builder()
                .email(usuario.getEmail().toLowerCase())
                .name(usuario.getName().toLowerCase())
                .lastname(usuario.getLastname().toLowerCase())
                .role(role)
                .status(Status.ACTIVE)
                .phone_contact(usuario.getPhone_contact().toLowerCase())
                .password(passwordEncoder.encode(usuario.getPassword()))
                .build();

        return usuariosRepository.save(u);
    }

    public Usuarios createUserAsClient(Usuarios usuario) throws BadRequestException {
        return saveUSer(usuario, UserRole.CLIENTE);
    }

    public Usuarios createUserAsLawyer(Usuarios usuario) throws BadRequestException {
        return saveUSer(usuario, UserRole.ABOGADO);

    }
    public Usuarios createUserAsAdmin(Usuarios usuario) throws BadRequestException {
        return saveUSer(usuario, UserRole.ADMIN);
    }

    private boolean isEmailInUse(String email){
        return usuariosRepository.findByEmail(email).isPresent();
    }

    /**public Optional<Usuarios> findUserById(String id){
        return usuarioRepository.findById(id);
    }**/

    private String getUserRole(String id){
        if (findUSerById(id).isPresent()){
            return findUSerById(id).get().getRole().toString();
        }
        else {
            return "User not found";
        }
    }
    public boolean isUserALawyerOrAdmin(String id){
        return getUserRole(id).equals("ABOGADO") || getUserRole(id).equals("ADMIN");
    }
    public boolean isUserAClient(String id){
        return getUserRole(id).equals("CLIENTE");
    }
    public Optional<Usuarios>findUSerById(String id){
        return usuariosRepository.findByuuidAndStatus(id, Status.ACTIVE);
    }
}
