package io.bootify.hotel_benidorm.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DescuentoDTO {

    private Integer idDescuento;

    @NotNull
    @Size(max = 255)
    private String tipo;

    @NotNull
    @Digits(integer = 7, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "55.08")
    private BigDecimal porcentaje;

    @NotNull
    private Boolean activo;

    @NotNull
    @Size(max = 255)
    private String codigoPromo;

}
