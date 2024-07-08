package com.consultorio.casos_judiciales.controllers;

import com.consultorio.casos_judiciales.models.Usuarios;
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

    //@PreAuthorize("hasAuthority('CREATE_PERSONA')")
    @PreAuthorize("permitAll")
    @PostMapping()
    public ResponseEntity<Usuarios>createClient(
            @Valid @RequestBody Usuarios usuarios, BindingResult result
    ) throws BadRequestException {
        HttpStatus status = HttpStatus.OK;
        if (result.hasErrors()){
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(usuarioService.createUserAsAdmin(usuarios), status);
    }
}
