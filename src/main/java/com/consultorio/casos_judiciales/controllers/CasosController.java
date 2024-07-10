package com.consultorio.casos_judiciales.controllers;

import com.consultorio.casos_judiciales.dtos.request.CasosRequest;
import com.consultorio.casos_judiciales.models.Caso;
import com.consultorio.casos_judiciales.services.CasosService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("cases")
public class CasosController {
    @Autowired
    private CasosService casosService;

    @PreAuthorize("hasAuthority('CREATE_CASE')")
    @PostMapping
    public ResponseEntity<Caso> createCase(
            @RequestHeader("Authorization")String bearerToken,
            @Valid @RequestBody CasosRequest request
            ) throws BadRequestException {
        String token = bearerToken.substring(7);
        return new ResponseEntity<>(casosService.createCase(request, token), HttpStatus.OK);
    }

    @PreAuthorize("permitAll")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Caso>> getCaseById(@PathVariable("id") int id){
        return ResponseEntity.ok(casosService.findCasosById(id));
    }
}
