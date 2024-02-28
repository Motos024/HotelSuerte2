package io.bootify.hotel_benidorm.repos;

import io.bootify.hotel_benidorm.domain.Descuento;
import io.bootify.hotel_benidorm.domain.Habitacion;
import io.bootify.hotel_benidorm.domain.Reserva;
import io.bootify.hotel_benidorm.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    Reserva findFirstByUsuario(Usuario usuario);

    Reserva findFirstByHabitacion(Habitacion habitacion);

    Reserva findFirstByDescuento(Descuento descuento);

}
