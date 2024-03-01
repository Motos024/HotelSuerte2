package io.bootify.hotel_benidorm.repos;

import io.bootify.hotel_benidorm.domain.Descuento;
import io.bootify.hotel_benidorm.domain.Habitacion;
import io.bootify.hotel_benidorm.domain.Reserva;
import io.bootify.hotel_benidorm.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    Reserva findFirstByUsuario(Usuario usuario);

    Reserva findFirstByHabitacion(Habitacion habitacion);

    Reserva findFirstByDescuento(Descuento descuento);

    @Query(value = "SELECT r.id_reserva " +
            "FROM reserva r " +
            "WHERE r.usuario_id = :idUsuario " +
            "ORDER BY r.fecha_inicio DESC " +
            "LIMIT 1", nativeQuery = true)
    Integer findUltimaReservaPorUsuarioNative(@Param("idUsuario") Integer idUsuario);


}
