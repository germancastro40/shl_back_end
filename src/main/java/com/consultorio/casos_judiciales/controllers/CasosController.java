package com.consultorio.casos_judiciales.controllers;

import com.consultorio.casos_judiciales.dtos.request.CasosRequest;
import com.consultorio.casos_judiciales.models.Casos;
import com.consultorio.casos_judiciales.services.CasosService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("cases")
public class CasosController {
    @Autowired
    private CasosService casosService;

    @PreAuthorize("hasAuthority('CREATE_CASE')")
    @PostMapping
    public ResponseEntity<Casos> createCase(
            @RequestHeader("Authorization")String bearerToken,
            @Valid @RequestBody CasosRequest request
            ) throws BadRequestException {
        String token = bearerToken.substring(7);
        return new ResponseEntity<>(casosService.createCase(request, token), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_ALL_CASES')")
    @GetMapping()
    public ResponseEntity<List<Casos>> getAllCases(){
        return ResponseEntity.ok().body(casosService.getAllCases());
    }

    @PreAuthorize("permitAll")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Casos>> getCaseById(@PathVariable("id") String id){
        Optional<Casos> caso = casosService.findCasosById(id);
        if (caso.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(caso, HttpStatus.OK);
    }
}