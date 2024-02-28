package io.bootify.hotel_benidorm.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RolDTO {

    private Integer idRol;

    @NotNull
    @Size(max = 255)
    private String tipoRol;

}
