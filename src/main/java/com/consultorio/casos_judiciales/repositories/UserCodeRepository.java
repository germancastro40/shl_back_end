package com.consultorio.casos_judiciales.repositories;

import com.consultorio.casos_judiciales.enums.CodeTypes;
import com.consultorio.casos_judiciales.enums.Status;
import com.consultorio.casos_judiciales.models.UsersCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserCodeRepository extends JpaRepository<UsersCodes, String>{
    Optional<UsersCodes> findByCode(String code);

    Optional<UsersCodes> findByCodeAndStatusAndCodeTypes(String code, Status status, CodeTypes codeTypes);
}
