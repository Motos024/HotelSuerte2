package io.bootify.hotel_benidorm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import io.bootify.hotel_benidorm.repos.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      return usuarioRepository.findByEmail(email)
              .map(usuario -> {
                  // Aquí asumimos que cada usuario tiene un único rol asignado.
                  // Se crea una autoridad basada en el tipo de rol del usuario.
                  List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getTipoRol()));

                  return new User(usuario.getEmail(),
                          usuario.getContrasena(),
                          authorities);
              })
              .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + email));
  }

}
