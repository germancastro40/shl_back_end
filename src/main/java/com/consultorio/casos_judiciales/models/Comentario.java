package com.consultorio.casos_judiciales.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity(name = "comentarios")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String comentario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_uuid", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caso_id", nullable = false)
    @JsonIgnore
    private Caso caso;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Temporal(TemporalType.DATE)
    @Column(updatable = false)
    @JsonIgnore
    private Date createdAt;

    @PrePersist
    private void onCreate(){
        Date now = new Date();
        createdAt = now;
    }

}
