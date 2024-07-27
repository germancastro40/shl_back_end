package com.consultorio.casos_judiciales.dtos.response;

import com.consultorio.casos_judiciales.models.Usuarios;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;



@AllArgsConstructor
@Data
public class ValidateTokenResponse  {
    private Usuarios usuario;
    private String token;
}
