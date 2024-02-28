package io.bootify.hotel_benidorm.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CaracteristicasHabitacionDTO {

    private Integer idCaracteristica;

    @NotNull
    @Size(max = 255)
    private String tipo;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "90.08")
    private BigDecimal precio;

    @Override
    public String toString() {
        return "CaracteristicasHabitacionDTO{" +
                "idCaracteristica=" + idCaracteristica +
                ", tipo='" + tipo + '\'' +
                ", precio=" + precio +
                '}';
    }

}
