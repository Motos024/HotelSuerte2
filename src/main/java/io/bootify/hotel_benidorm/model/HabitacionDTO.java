package io.bootify.hotel_benidorm.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class HabitacionDTO {

    private Integer idHabitacion;

    @NotNull
    private Integer planta;

    private Integer caracteristica;

}
