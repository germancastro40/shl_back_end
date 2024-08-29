package com.consultorio.casos_judiciales.repositories;

import com.consultorio.casos_judiciales.models.Casos;
import com.consultorio.casos_judiciales.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CasosRepository extends JpaRepository<Casos, String> {
    Optional<Casos> findCasoByIdAndStatus(String id, Status status);
}
