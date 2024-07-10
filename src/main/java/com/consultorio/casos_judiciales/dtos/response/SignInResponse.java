package com.consultorio.casos_judiciales.dtos.response;

import com.consultorio.casos_judiciales.models.Usuario;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SignInResponse {
    private Usuario usuario;
    private String token;
}
