package io.bootify.hotel_benidorm.repos;

import io.bootify.hotel_benidorm.domain.Descuento;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DescuentoRepository extends JpaRepository<Descuento, Integer> {
}
