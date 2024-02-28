package io.bootify.hotel_benidorm.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // Permite el acceso sin autenticación a las rutas especificadas
                        .requestMatchers("/", "/home", "/css/**", "/js/**", "/images/**", "/login", "usuarios/add").permitAll()
                        // Todas las demás solicitudes requieren autenticación
                        .requestMatchers("/usuarios/**").hasAuthority("ROLE_Admin")
                        .requestMatchers("/reservas/**").hasAuthority("ROLE_Cliente")
                        .requestMatchers("/servicioss/**").hasAuthority("ROLE_Empleado")
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true) // Redirigir al usuario a esta página después del login exitoso
                        .failureUrl("/login?error=true") // Página o ruta a la que redirigir en caso de fallo
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }
}

