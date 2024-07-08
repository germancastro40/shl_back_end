package com.consultorio.casos_judiciales.repositories;

import com.consultorio.casos_judiciales.models.Comentarios;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComentariosRepository extends JpaRepository<Comentarios, String> {
}
