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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Usuarios>createAbogado(@Valid @RequestBody Usuarios usuario) throws BadRequestException {
        return new ResponseEntity<>(usuarioService.createUserAsLawyer(usuario), HttpStatus.CREATED);

    }

    @PreAuthorize("hasAuthority('CREATE_PERSONA')")
    @PostMapping("cliente")
    public ResponseEntity<Usuarios>createClient(@Valid @RequestBody Usuarios usuario) throws BadRequestException {
        return new ResponseEntity<>(usuarioService.createUserAsClient(usuario), HttpStatus.CREATED);

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {

        String actualToken = token.substring(7);

        ValidateTokenResponse response = authService.validateToken(actualToken);

        if (response.getUsuario() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return ResponseEntity.ok(response);
    }

}
