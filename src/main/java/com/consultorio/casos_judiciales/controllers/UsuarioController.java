package com.consultorio.casos_judiciales.controllers;

import com.consultorio.casos_judiciales.models.Usuario;
import com.consultorio.casos_judiciales.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("client")
@AllArgsConstructor
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @PreAuthorize("hasAuthority('CREATE_ADMIN')")
    @PostMapping("admin")
    public ResponseEntity<Usuario>createAdmin(
            @Valid @RequestBody Usuario usuario, BindingResult result
    ) throws BadRequestException {
        HttpStatus status = HttpStatus.OK;
        if (result.hasErrors()){
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(usuarioService.createUserAsAdmin(usuario), status);
    }

    @PreAuthorize("hasAuthority('CREATE_ABOGADO')")
    @PostMapping("abogado")
    public ResponseEntity<Usuario>createAbogado(@Valid @RequestBody Usuario usuario) throws BadRequestException {
        return new ResponseEntity<>(usuarioService.createUserAsLawyer(usuario), HttpStatus.CREATED);

    }

    @PreAuthorize("hasAuthority('CREATE_PERSONA')")
    @PostMapping("cliente")
    public ResponseEntity<Usuario>createClient(@Valid @RequestBody Usuario usuario) throws BadRequestException {
        return new ResponseEntity<>(usuarioService.createUserAsClient(usuario), HttpStatus.CREATED);

    }

}
