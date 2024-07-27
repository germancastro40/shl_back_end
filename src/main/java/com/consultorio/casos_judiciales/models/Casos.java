package com.consultorio.casos_judiciales.models;

import com.consultorio.casos_judiciales.models.Comentarios;
import com.consultorio.casos_judiciales.models.Usuarios;
import com.consultorio.casos_judiciales.utils.EstadoCasos;
import com.consultorio.casos_judiciales.utils.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Casos {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private String id;

        @NotNull
        @Column(nullable = false)
        private String descripcion;

        private String nombreCaso;

        @NotNull
        @Enumerated(EnumType.STRING)
        private EstadoCasos estado;

        @ManyToOne
        @JoinColumn(name = "cliente_id", referencedColumnName = "uuid")
        private Usuarios cliente;

        @ManyToOne
        @JoinColumn(name = "abogado_id", referencedColumnName = "uuid")
        private Usuarios abogado;

        @OneToMany(mappedBy = "caso", cascade = CascadeType.ALL)
        private List<Comentarios> comentarios;

        @Enumerated(EnumType.STRING)
        @JsonIgnore
        private Status status;

        @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
        @Temporal(TemporalType.TIMESTAMP)
        private Date createdAt;

        @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
        @Temporal(TemporalType.TIMESTAMP)
        private Date updatedAt;

        @PrePersist
        private void onCreate(){
                Date now = new Date();
                createdAt = now;
                updatedAt = now;
        }

        @PostUpdate
        private void onUpdate(){
                updatedAt = new Date();
        }
}
