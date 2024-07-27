package com.consultorio.casos_judiciales.dtos.response;

import com.consultorio.casos_judiciales.models.Usuarios;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SignInResponse {
    private Usuarios usuario;
    private String token;
}
