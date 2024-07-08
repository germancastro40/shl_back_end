package com.consultorio.casos_judiciales.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CasosRequest {
    private String nombreCaso;
    private String descripcion;
    private String cliente_id;
}
