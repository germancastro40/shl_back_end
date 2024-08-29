package com.consultorio.casos_judiciales.controllers;

import com.consultorio.casos_judiciales.dtos.response.ValidateTokenResponse;
import com.consultorio.casos_judiciales.models.Usuarios;
import com.consultorio.casos_judiciales.services.AuthService;
import com.consultorio.casos_judiciales.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("client")
@AllArgsConstructor
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthService authService;

    @PreAuthorize("hasAuthority('CREATE_ADMIN')")
    @PostMapping("admin")
    public ResponseEntity<Usuarios>createAdmin(
            @Valid @RequestBody Usuarios usuario, BindingResult result
    ) throws BadRequestException {
        HttpStatus status = HttpStatus.OK;
        if (result.hasErrors()){
            throw new BadRequestException(result.getFieldError().getDefaultMessage().toString());
        }
        return new ResponseEntity<>(usuarioService.createUserAsAdmin(usuario), status);
    }

    @PreAuthorize("hasAuthority('CREATE_ABOGADO')")
    @PostMapping("abogado")
    public ResponseEntity<Usuarios>createAbogado(
            @Valid @RequestBody Usuarios usuario, BindingResult result
    ) throws BadRequestException {
        return new ResponseEntity<>(usuarioService.createUserAsLawyer(usuario), HttpStatus.CREATED);

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Usuarios>> getUserById(@PathVariable("id")String id){
        return ResponseEntity.ok(usuarioService.findUSerById(id));
    }

    @PreAuthorize("hasAuthority('CREATE_PERSONA')")
    @PostMapping("cliente")
    public ResponseEntity<Usuarios>createClient(@Valid @RequestBody Usuarios usuario) throws BadRequestException {
        return new ResponseEntity<>(usuarioService.createUserAsClient(usuario), HttpStatus.CREATED);

    }

    @PreAuthorize("hasAuthority('READ_ALL_CLIENT_ROLE')")
    @GetMapping("list/client")
    public ResponseEntity<List<Usuarios>>getClienteRole(){
        return ResponseEntity.ok(usuarioService.getClientRoleList());
    }

    @PreAuthorize("hasAuthority('READ_ALL_CLIENT')")
    @GetMapping("/all")
    public ResponseEntity<List<Usuarios>>getAllUsers(){
        return ResponseEntity.ok(usuarioService.getAllUsers());
    }

    @PreAuthorize("hasAuthority('UPDATE_PERSONA')")
    @PatchMapping("{id}")
    public ResponseEntity<Usuarios>updateUser(
            @PathVariable("id") String id,
            @Valid @RequestBody Usuarios usuarios
    ){
        return ResponseEntity.ok(usuarioService.updateCliente(usuarios, id));
    }
    @PreAuthorize("hasAuthority('DELETE_PERSONA')")
    @DeleteMapping("{id}")
    public ResponseEntity<Map<String,String>> deleteClientByID(
            @PathVariable("id")String id
    ){
        HttpStatus status = HttpStatus.OK;

        if (usuarioService.findUSerById(id).isEmpty()){
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>( usuarioService.deleteClientByID(id), status );
    }
}
