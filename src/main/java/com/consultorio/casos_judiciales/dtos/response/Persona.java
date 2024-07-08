package com.consultorio.casos_judiciales.dtos.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
@Data
@Builder
public class Persona {
    private String id;
    private String email;
    private String nombre;
    private String apellido;
    private BigInteger phoneContact;

}
