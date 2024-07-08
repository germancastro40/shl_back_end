package com.consultorio.casos_judiciales.repositories;

import com.consultorio.casos_judiciales.models.Casos;
import com.consultorio.casos_judiciales.utils.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CasosRepository extends JpaRepository<Casos, Integer> {
    Optional<Casos> findCasoByIdAndStatus(int id, Status status);
}
