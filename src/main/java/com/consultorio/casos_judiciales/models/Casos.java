package com.consultorio.casos_judiciales.models;

import com.consultorio.casos_judiciales.utils.EstadoCasos;
import com.consultorio.casos_judiciales.utils.Status;
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
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Casos {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private int id;

        @NotNull
        @Column(nullable = false)
        private String descripcion;

        private String nombreCaso;

        @NotNull
        @Enumerated(EnumType.STRING)
        private EstadoCasos estado;
        /**
         * private List<Comentarios> etapa;
         * TODO: agregar este punto
         * los comentarios deben tener informacion como por ejemplo;
         * quien comenta a que hora y fecha comento y ordenarlos de el primero a el ultimo
         *
         */
        @ManyToOne
        @JoinColumn(name = "cliente_id", referencedColumnName = "uuid")
        //@JsonIgnore
        private Usuarios cliente;

        @ManyToOne
        @JoinColumn(name = "abogado_id", referencedColumnName = "uuid")
        //@JsonIgnore
        private Usuarios abogado;

        @OneToMany(mappedBy = "caso", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Comentarios> comentarios;

        @Enumerated(EnumType.STRING)
        @JsonIgnore
        private Status status;

        @DateTimeFormat(pattern = "yyyy/MM/dd")
        @Temporal(TemporalType.DATE)
        private Date createdAt;

        @DateTimeFormat(pattern = "yyyy/MM/dd")
        @Temporal(TemporalType.DATE)
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

