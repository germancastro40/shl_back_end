package com.consultorio.casos_judiciales.controllers;

import com.consultorio.casos_judiciales.dtos.request.SignInRequest;
import com.consultorio.casos_judiciales.dtos.response.SignInResponse;
import com.consultorio.casos_judiciales.dtos.response.ValidateTokenResponse;
import com.consultorio.casos_judiciales.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PreAuthorize("permitAll")
    @PostMapping("signIn")
    public ResponseEntity<SignInResponse> signIn(
            @Valid @RequestBody SignInRequest request, BindingResult result
            ){
        HttpStatus status = HttpStatus.OK;

        if (result.hasErrors()){
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(authService.signIn(request), status);
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

    @PreAuthorize("permitAll")
    @GetMapping(value = "/hola")
    public ResponseEntity<?> resp(@RequestBody String msg) {
        return ResponseEntity.ok(msg);
    }

}
