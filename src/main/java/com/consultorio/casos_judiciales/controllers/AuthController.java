package com.consultorio.casos_judiciales.controllers;

import com.consultorio.casos_judiciales.dtos.request.SignInRequest;
import com.consultorio.casos_judiciales.dtos.response.SignInResponse;
import com.consultorio.casos_judiciales.services.AuthService;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("auth")
@AllArgsConstructor
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
}
