package io.bootify.hotel_benidorm.repos;

import io.bootify.hotel_benidorm.domain.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByTipoRol(String tipoRol);

}
