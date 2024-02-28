package io.bootify.hotel_benidorm.repos;

import io.bootify.hotel_benidorm.domain.CaracteristicasHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface CaracteristicasHabitacionRepository extends JpaRepository<CaracteristicasHabitacion, Integer> {
    
}
