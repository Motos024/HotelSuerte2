package io.bootify.hotel_benidorm.repos;

import io.bootify.hotel_benidorm.domain.Reserva;
import io.bootify.hotel_benidorm.domain.Servicios;
import io.bootify.hotel_benidorm.domain.ServiciosContratados;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ServiciosContratadosRepository extends JpaRepository<ServiciosContratados, Integer> {

    ServiciosContratados findFirstByReserva(Reserva reserva);

    ServiciosContratados findFirstByServicio(Servicios servicios);

}
