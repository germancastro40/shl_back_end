package com.consultorio.casos_judiciales.services;
import com.consultorio.casos_judiciales.models.Usuarios;
import com.consultorio.casos_judiciales.repositories.UsuariosRepository;
import com.consultorio.casos_judiciales.enums.Status;
import com.consultorio.casos_judiciales.enums.UserRole;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsuarioService {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersCodeService usersCodeService;

    public List<Usuarios> getAllUsers(){
        List<Usuarios> usuariosList = usuariosRepository.findAll();
        usuariosList.removeIf(usuario ->
            usuario.getStatus() == Status.DISABLE
        );
        return usuariosList;
    }

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
                .status(Status.DISABLE)
                .phone_contact(usuario.getPhone_contact().toLowerCase())
                .password(passwordEncoder.encode("usuarionuevo"))
                .build();
        try {
            usersCodeService.generateCodeForActivateUser(email);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
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

    public boolean isEmailInUse(String email){
        return usuariosRepository.findByEmail(email).isPresent();
    }


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

    public List<Usuarios> getClientRoleList(){
        return usuariosRepository.findByRoleAndStatus(UserRole.CLIENTE, Status.ACTIVE);
    }

    public Usuarios updateCliente(Usuarios usuarios, String id) {
        Usuarios oldUser = findUSerById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        oldUser.setEmail(usuarios.getEmail());
        oldUser.setName(usuarios.getName());
        oldUser.setLastname(usuarios.getLastname());
        oldUser.setPhone_contact(usuarios.getPhone_contact());

        return usuariosRepository.save(oldUser);
    }

    public Map<String, String> deleteClientByID(String id) {
        Map<String, String> response = new HashMap<>();
        if (findUSerById(id).isEmpty()){
            response.put("error", "User not found");
        }
        Usuarios usuarios = findUSerById(id).get();

        usuarios.setStatus(Status.DISABLE);
        usuariosRepository.save(usuarios);

        response.put("message", "User disabled successfully");
        return response;
    }
}
