package com.consultorio.casos_judiciales.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SignInRequest {
    @NotEmpty
    @Email
    String email;
    @NotEmpty
    String password;
}
