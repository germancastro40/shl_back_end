package com.consultorio.casos_judiciales.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComentariosRequest {
    @NotNull(message = "comentarios can't be null")
    private String comentario;
}
