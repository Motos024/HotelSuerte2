package io.bootify.hotel_benidorm.service;

import io.bootify.hotel_benidorm.domain.Reserva;
import io.bootify.hotel_benidorm.domain.Rol;
import io.bootify.hotel_benidorm.domain.Usuario;
import io.bootify.hotel_benidorm.model.UsuarioDTO;
import io.bootify.hotel_benidorm.repos.ReservaRepository;
import io.bootify.hotel_benidorm.repos.RolRepository;
import io.bootify.hotel_benidorm.repos.UsuarioRepository;
import io.bootify.hotel_benidorm.util.NotFoundException;
import io.bootify.hotel_benidorm.util.ReferencedWarning;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final ReservaRepository reservaRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(final UsuarioRepository usuarioRepository,
                          final RolRepository rolRepository,
                          final ReservaRepository reservaRepository,
                          final PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.reservaRepository = reservaRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public List<UsuarioDTO> findAll() {
        final List<Usuario> usuarios = usuarioRepository.findAll(Sort.by("idUsuario"));
        return usuarios.stream()
                .map(usuario -> mapToDTO(usuario, new UsuarioDTO()))
                .toList();
    }

    public UsuarioDTO get(final Integer idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .map(usuario -> mapToDTO(usuario, new UsuarioDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final UsuarioDTO usuarioDTO) {
        final Usuario usuario = new Usuario();
        mapToEntity(usuarioDTO, usuario);
        // Cifrar la contraseña antes de guardar
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        return usuarioRepository.save(usuario).getIdUsuario();
    }

    public void update(final Integer idUsuario, final UsuarioDTO usuarioDTO) {
        final Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(NotFoundException::new);
        mapToEntity(usuarioDTO, usuario);
        // Cifrar la contraseña solo si se proporciona una nueva
        if (usuarioDTO.getContrasena() != null && !usuarioDTO.getContrasena().isBlank()) {
            usuario.setContrasena(passwordEncoder.encode(usuarioDTO.getContrasena()));
        }
        usuarioRepository.save(usuario);
    }

    public void delete(final Integer idUsuario) {
        usuarioRepository.deleteById(idUsuario);
    }

    private UsuarioDTO mapToDTO(final Usuario usuario, final UsuarioDTO usuarioDTO) {
        usuarioDTO.setIdUsuario(usuario.getIdUsuario());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setApellido(usuario.getApellido());
        // No devolver la contraseña
        usuarioDTO.setContrasena(null); // O simplemente omite esta línea
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setTelefono(usuario.getTelefono());
        usuarioDTO.setRol(usuario.getRol() == null ? null : usuario.getRol().getIdRol());
        return usuarioDTO;
    }

    private Usuario mapToEntity(final UsuarioDTO usuarioDTO, final Usuario usuario) {
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setContrasena(usuarioDTO.getContrasena());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setTelefono(usuarioDTO.getTelefono());
        final Rol rol = usuarioDTO.getRol() == null ? null : rolRepository.findById(usuarioDTO.getRol())
                .orElseThrow(() -> new NotFoundException("rol not found"));
        usuario.setRol(rol);
        return usuario;
    }

    public ReferencedWarning getReferencedWarning(final Integer idUsuario) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(NotFoundException::new);
        final Reserva usuarioReserva = reservaRepository.findFirstByUsuario(usuario);
        if (usuarioReserva != null) {
            referencedWarning.setKey("usuario.reserva.usuario.referenced");
            referencedWarning.addParam(usuarioReserva.getIdReserva());
            return referencedWarning;
        }
        return null;
    }

}
