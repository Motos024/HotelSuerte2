package io.bootify.hotel_benidorm.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UsuarioDTO {

    private Integer idUsuario;

    @NotNull
    @Size(max = 255)
    private String nombre;

    @NotNull
    @Size(max = 255)
    private String apellido;

    @NotNull
    @Size(max = 255)
    private String contrasena;

    @NotNull
    @Size(max = 255)
    private String email;

    @Size(max = 20)
    private String telefono;

    private Integer rol;

}
