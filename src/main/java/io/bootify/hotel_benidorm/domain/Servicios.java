package io.bootify.hotel_benidorm.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Servicios {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idServicio;

    @Column(nullable = false)
    private String tipoServicio;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @OneToMany(mappedBy = "servicio")
    private Set<ServiciosContratados> servicioServiciosContratadoses;

}
