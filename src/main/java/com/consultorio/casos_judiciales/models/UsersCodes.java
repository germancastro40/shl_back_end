package com.consultorio.casos_judiciales.models;

import com.consultorio.casos_judiciales.enums.CodeTypes;
import com.consultorio.casos_judiciales.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersCodes {
    @Id
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario", nullable = false)
    private Usuarios usuarios;

    @Enumerated(EnumType.STRING)
    private CodeTypes codeTypes;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredAt;

    @Enumerated(EnumType.STRING)
    private Status status;
}