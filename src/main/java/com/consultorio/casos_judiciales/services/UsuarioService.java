package com.consultorio.casos_judiciales.services;

import com.consultorio.casos_judiciales.models.Usuarios;
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

    private Usuarios saveUSer(Usuarios usuarios, UserRole role) throws BadRequestException {
        String email = usuarios.getEmail();
        if (isEmailInUse(email)){
            throw new BadRequestException("The email address: '"+email.toUpperCase()+"' is already in use");
        }
        usuarios.setRole(role);
        usuarios.setStatus(Status.ACTIVE);
        usuarios.setPassword(passwordEncoder.encode(usuarios.getPassword()));
        return usuarioRepository.save(usuarios);
    }
    public Usuarios createUserAsClient(Usuarios usuarios) throws BadRequestException {
        return saveUSer(usuarios, UserRole.CLIENTE);
    }

    public Usuarios createUserAsLawyer(Usuarios usuarios) throws BadRequestException {
        return saveUSer(usuarios, UserRole.ABOGADO);

    }
    public Usuarios createUserAsAdmin(Usuarios usuarios) throws BadRequestException {
        return saveUSer(usuarios, UserRole.ADMIN);
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
    public Optional<Usuarios>findUSerByIDActive(String id){
        return usuarioRepository.findByuuidAndStatus(id, Status.ACTIVE);
    }
}
