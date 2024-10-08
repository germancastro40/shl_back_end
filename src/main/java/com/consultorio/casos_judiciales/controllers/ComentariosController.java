package com.consultorio.casos_judiciales.controllers;

import com.consultorio.casos_judiciales.dtos.request.ComentariosRequest;
import com.consultorio.casos_judiciales.models.Comentarios;
import com.consultorio.casos_judiciales.services.ComentarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comentarios")
@AllArgsConstructor
public class ComentariosController {

    @Autowired
    private ComentarioService comentarioService;
    @PreAuthorize("permitAll")
    @PostMapping("/{id}")
    public ResponseEntity<Comentarios> generateComentario(
            @Valid @RequestBody ComentariosRequest request,
            @RequestHeader("Authorization") String bearerToken,
            @PathVariable("id")String id
    ) throws BadRequestException {
        String token = bearerToken.substring(7);
        return ResponseEntity.ok(comentarioService.generateComentario(request, token, id));
    }
}
