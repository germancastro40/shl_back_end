package com.consultorio.casos_judiciales.repositories;

import com.consultorio.casos_judiciales.models.Usuarios;
import com.consultorio.casos_judiciales.utils.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuarios, String> {
    Optional<Usuarios>findByEmail(String email);
    Optional<Usuarios> findByEmailAndStatus(String Email, Status status);
    Optional<Usuarios>findByuuidAndStatus(String uuid, Status status);
}

