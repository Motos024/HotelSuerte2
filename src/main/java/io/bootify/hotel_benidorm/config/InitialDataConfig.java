package io.bootify.hotel_benidorm.config;

import io.bootify.hotel_benidorm.domain.Rol;
import io.bootify.hotel_benidorm.repos.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitialDataConfig {

    @Bean
    CommandLineRunner initRoles(RolRepository rolRepository) {
        return args -> {
            if (rolRepository.findByTipoRol("Admin").isEmpty()) {
                Rol adminRol = new Rol();
                adminRol.setTipoRol("Admin");
                rolRepository.save(adminRol);
            }
            if (rolRepository.findByTipoRol("Cliente").isEmpty()) {
                Rol clienteRol = new Rol();
                clienteRol.setTipoRol("Cliente");
                rolRepository.save(clienteRol);
            }
            if (rolRepository.findByTipoRol("Empleado").isEmpty()) {
                Rol empleadoRol = new Rol();
                empleadoRol.setTipoRol("Empleado");
                rolRepository.save(empleadoRol);
            }
        };
    }
}
