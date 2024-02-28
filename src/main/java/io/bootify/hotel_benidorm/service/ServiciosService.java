package io.bootify.hotel_benidorm.service;

import io.bootify.hotel_benidorm.domain.Servicios;
import io.bootify.hotel_benidorm.domain.ServiciosContratados;
import io.bootify.hotel_benidorm.model.ServiciosDTO;
import io.bootify.hotel_benidorm.repos.ServiciosContratadosRepository;
import io.bootify.hotel_benidorm.repos.ServiciosRepository;
import io.bootify.hotel_benidorm.util.NotFoundException;
import io.bootify.hotel_benidorm.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ServiciosService {

    private final ServiciosRepository serviciosRepository;
    private final ServiciosContratadosRepository serviciosContratadosRepository;

    public ServiciosService(final ServiciosRepository serviciosRepository,
            final ServiciosContratadosRepository serviciosContratadosRepository) {
        this.serviciosRepository = serviciosRepository;
        this.serviciosContratadosRepository = serviciosContratadosRepository;
    }

    public List<ServiciosDTO> findAll() {
        final List<Servicios> servicioses = serviciosRepository.findAll(Sort.by("idServicio"));
        return servicioses.stream()
                .map(servicios -> mapToDTO(servicios, new ServiciosDTO()))
                .toList();
    }

    public ServiciosDTO get(final Integer idServicio) {
        return serviciosRepository.findById(idServicio)
                .map(servicios -> mapToDTO(servicios, new ServiciosDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ServiciosDTO serviciosDTO) {
        final Servicios servicios = new Servicios();
        mapToEntity(serviciosDTO, servicios);
        return serviciosRepository.save(servicios).getIdServicio();
    }

    public void update(final Integer idServicio, final ServiciosDTO serviciosDTO) {
        final Servicios servicios = serviciosRepository.findById(idServicio)
                .orElseThrow(NotFoundException::new);
        mapToEntity(serviciosDTO, servicios);
        serviciosRepository.save(servicios);
    }

    public void delete(final Integer idServicio) {
        serviciosRepository.deleteById(idServicio);
    }

    private ServiciosDTO mapToDTO(final Servicios servicios, final ServiciosDTO serviciosDTO) {
        serviciosDTO.setIdServicio(servicios.getIdServicio());
        serviciosDTO.setTipoServicio(servicios.getTipoServicio());
        serviciosDTO.setPrecio(servicios.getPrecio());
        return serviciosDTO;
    }

    private Servicios mapToEntity(final ServiciosDTO serviciosDTO, final Servicios servicios) {
        servicios.setTipoServicio(serviciosDTO.getTipoServicio());
        servicios.setPrecio(serviciosDTO.getPrecio());
        return servicios;
    }

    public ReferencedWarning getReferencedWarning(final Integer idServicio) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Servicios servicios = serviciosRepository.findById(idServicio)
                .orElseThrow(NotFoundException::new);
        final ServiciosContratados servicioServiciosContratados = serviciosContratadosRepository.findFirstByServicio(servicios);
        if (servicioServiciosContratados != null) {
            referencedWarning.setKey("servicios.serviciosContratados.servicio.referenced");
            referencedWarning.addParam(servicioServiciosContratados.getIdServicioContratado());
            return referencedWarning;
        }
        return null;
    }

}
