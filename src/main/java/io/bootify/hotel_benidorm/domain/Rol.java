package io.bootify.hotel_benidorm.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor // Para el constructor sin argumentos
@RequiredArgsConstructor // Para el constructor con campos requeridos
public class Rol {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRol;

    @NonNull // Marca tipoRol como necesario para el constructor @RequiredArgsConstructor
    @Column(nullable = false)
    private String tipoRol;

    @OneToMany(mappedBy = "rol")
    private Set<Usuario> rolUsuarios;

}
