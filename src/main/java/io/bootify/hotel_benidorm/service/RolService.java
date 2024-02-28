package io.bootify.hotel_benidorm.service;

import io.bootify.hotel_benidorm.domain.Rol;
import io.bootify.hotel_benidorm.domain.Usuario;
import io.bootify.hotel_benidorm.model.RolDTO;
import io.bootify.hotel_benidorm.repos.RolRepository;
import io.bootify.hotel_benidorm.repos.UsuarioRepository;
import io.bootify.hotel_benidorm.util.NotFoundException;
import io.bootify.hotel_benidorm.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class RolService {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;

    public RolService(final RolRepository rolRepository,
            final UsuarioRepository usuarioRepository) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<RolDTO> findAll() {
        final List<Rol> rols = rolRepository.findAll(Sort.by("idRol"));
        return rols.stream()
                .map(rol -> mapToDTO(rol, new RolDTO()))
                .toList();
    }

    public RolDTO get(final Integer idRol) {
        return rolRepository.findById(idRol)
                .map(rol -> mapToDTO(rol, new RolDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final RolDTO rolDTO) {
        final Rol rol = new Rol();
        mapToEntity(rolDTO, rol);
        return rolRepository.save(rol).getIdRol();
    }

    public void update(final Integer idRol, final RolDTO rolDTO) {
        final Rol rol = rolRepository.findById(idRol)
                .orElseThrow(NotFoundException::new);
        mapToEntity(rolDTO, rol);
        rolRepository.save(rol);
    }

    public void delete(final Integer idRol) {
        rolRepository.deleteById(idRol);
    }

    private RolDTO mapToDTO(final Rol rol, final RolDTO rolDTO) {
        rolDTO.setIdRol(rol.getIdRol());
        rolDTO.setTipoRol(rol.getTipoRol());
        return rolDTO;
    }

    private Rol mapToEntity(final RolDTO rolDTO, final Rol rol) {
        rol.setTipoRol(rolDTO.getTipoRol());
        return rol;
    }

    public ReferencedWarning getReferencedWarning(final Integer idRol) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Rol rol = rolRepository.findById(idRol)
                .orElseThrow(NotFoundException::new);
        final Usuario rolUsuario = usuarioRepository.findFirstByRol(rol);
        if (rolUsuario != null) {
            referencedWarning.setKey("rol.usuario.rol.referenced");
            referencedWarning.addParam(rolUsuario.getIdUsuario());
            return referencedWarning;
        }
        return null;
    }

}
