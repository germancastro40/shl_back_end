package com.consultorio.casos_judiciales.enums;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum UserRole {
    ABOGADO(List.of( Permission.CREATE_PERSONA, Permission.UPDATE_CASE,
                     Permission.READ_CASE,      Permission.CREATE_CASE,
                     Permission.READ_ALL_CASES, Permission.READ_ALL_CLIENT_ROLE ) ),

    CLIENTE(List.of( Permission.READ_CASE, Permission.UPDATE_PERSONA) ),

    ADMIN(List.of( Permission.CREATE_CASE,        Permission.DELETE_CASE,
                   Permission.UPDATE_CASE,        Permission.READ_CASE,
                   Permission.CREATE_PERSONA,     Permission.READ_PERSONA,
                   Permission.DELETE_CASE,        Permission.UPDATE_PERSONA,
                   Permission.DELETE_PERSONA,     Permission.CREATE_ADMIN,
                   Permission.CREATE_ABOGADO,     Permission.READ_ALL_CASES,
                   Permission.READ_ALL_CLIENT_ROLE, Permission.READ_ALL_CLIENT
    ));

    private List<Permission>permissions;
}
