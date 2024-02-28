package io.bootify.hotel_benidorm.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;


@Getter
@Setter
public class ReservaDTO {

    private Integer idReserva;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "16.08")
    private BigDecimal precioFinal;

    @NotNull
    private Integer adulto;

    @NotNull
    private Integer ninos;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime fechaInicio;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime fechaFin;

    private Integer usuario;

    private Integer habitacion;

    private Integer descuento;

    @Override
    public String toString() {
        return "ReservaDTO{" +
                "idReserva=" + idReserva +
                ", precioFinal=" + precioFinal +
                ", adulto=" + adulto +
                ", ninos=" + ninos +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", usuario=" + usuario +
                ", habitacion=" + habitacion +
                ", descuento=" + descuento +
                '}';
    }

}

