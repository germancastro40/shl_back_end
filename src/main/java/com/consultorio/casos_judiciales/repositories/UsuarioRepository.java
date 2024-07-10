package com.consultorio.casos_judiciales.repositories;

import com.consultorio.casos_judiciales.models.Usuario;
import com.consultorio.casos_judiciales.utils.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario>findByEmail(String email);
    Optional<Usuario> findByEmailAndStatus(String Email, Status status);
    Optional<Usuario>findByuuidAndStatus(String uuid, Status status);
}

