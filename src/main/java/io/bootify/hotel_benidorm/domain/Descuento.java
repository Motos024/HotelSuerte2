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
public class Descuento {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDescuento;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal porcentaje;

    @Column(nullable = false)
    private Boolean activo;

    @Column(nullable = false)
    private String codigoPromo;

    @OneToMany(mappedBy = "descuento")
    private Set<Reserva> descuentoReservas;

}
