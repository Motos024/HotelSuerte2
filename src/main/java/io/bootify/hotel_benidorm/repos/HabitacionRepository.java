package io.bootify.hotel_benidorm.repos;

import io.bootify.hotel_benidorm.domain.CaracteristicasHabitacion;
import io.bootify.hotel_benidorm.domain.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


public interface HabitacionRepository extends JpaRepository<Habitacion, Integer> {

    Habitacion findFirstByCaracteristica(CaracteristicasHabitacion caracteristicasHabitacion);
  @Query(value = "SELECT MIN(h.id_habitacion) AS id_habitacion_disponible " +
          "FROM habitacion h " +
          "WHERE h.caracteristica_id = :idCaracteristica " +
          "AND NOT EXISTS (" +
          "    SELECT 1 FROM reserva r " +
          "    WHERE r.habitacion_id = h.id_habitacion " +
          "    AND r.fecha_inicio < :fechaFinDeseada " +
          "    AND r.fecha_fin > :fechaInicioDeseada) " +
          "GROUP BY h.caracteristica_id", nativeQuery = true)
  Integer findHabitacionesDisponiblesPorCaracteristicaNative(
          @Param("idCaracteristica") Integer idCaracteristica,
          @Param("fechaInicioDeseada") Date fechaInicioDeseada,
          @Param("fechaFinDeseada") Date fechaFinDeseada);





}
