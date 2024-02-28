package io.bootify.hotel_benidorm.repos;

import io.bootify.hotel_benidorm.domain.Rol;
import io.bootify.hotel_benidorm.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
    Usuario findFirstByRol(Rol rol);

    @Query("SELECT u.idUsuario FROM Usuario u WHERE u.email = :email")
    Integer findIdByEmail(String email);


}
