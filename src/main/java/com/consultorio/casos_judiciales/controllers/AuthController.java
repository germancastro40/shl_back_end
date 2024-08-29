package com.consultorio.casos_judiciales.controllers;

import com.consultorio.casos_judiciales.dtos.request.NewPasswordRequest;
import com.consultorio.casos_judiciales.dtos.request.SignInRequest;
import com.consultorio.casos_judiciales.dtos.response.SignInResponse;
import com.consultorio.casos_judiciales.dtos.response.ValidateTokenResponse;
import com.consultorio.casos_judiciales.services.AuthService;
import com.consultorio.casos_judiciales.services.UsersCodeService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UsersCodeService usersCodeService;

    @PreAuthorize("permitAll")
    @PostMapping("signIn")
    public ResponseEntity<?> signIn(
            @Valid @RequestBody SignInRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        SignInResponse response = authService.signIn(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {

        String actualToken = token.substring(7);

        ValidateTokenResponse response = authService.validateToken(actualToken);

        if (response.getUsuario() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("permitAll")
    @GetMapping("forgotPassword")
    public ResponseEntity<Map<String, String>>forgotPassword(@RequestParam String email) throws BadRequestException, MessagingException {
        Map<String, String> resp = new HashMap<>();
        resp.put("msg", usersCodeService.generateCodeForResetPassword(email));
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PreAuthorize("permitAll")
    @GetMapping("activateUser")
    public ResponseEntity<Map<String, String>>activateUser(@RequestParam String email) throws BadRequestException, MessagingException {
        Map<String, String> resp = new HashMap<>();
        resp.put("msg", usersCodeService.generateCodeForActivateUser(email));
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PreAuthorize("permitAll")
    @PutMapping("set-new-password")
    public ResponseEntity<Map<String, String>>setPassword(@RequestBody NewPasswordRequest newPasswordRequest) throws BadRequestException {
        Map<String,String> resp = new HashMap<>();
        resp.put("msg", authService.changeUsersPassword(newPasswordRequest));
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PreAuthorize("permitAll")
    @PutMapping("activateUser")
    public ResponseEntity<Map<String, String>>activateUserAndSetPassword(@RequestBody NewPasswordRequest newPasswordRequest) throws BadRequestException {
        Map<String,String> resp = new HashMap<>();
        resp.put("msg", authService.activateUser(newPasswordRequest));
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
}
