package com.consultorio.casos_judiciales.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NewPasswordRequest {

    private String code;
    private String newPassword;
}
