package io.bootify.hotel_benidorm.service;

import io.bootify.hotel_benidorm.domain.CaracteristicasHabitacion;
import io.bootify.hotel_benidorm.domain.Reserva;
import io.bootify.hotel_benidorm.domain.Servicios;
import io.bootify.hotel_benidorm.domain.ServiciosContratados;
import io.bootify.hotel_benidorm.model.CaracteristicasHabitacionDTO;
import io.bootify.hotel_benidorm.model.ServiciosContratadosDTO;
import io.bootify.hotel_benidorm.repos.ReservaRepository;
import io.bootify.hotel_benidorm.repos.ServiciosContratadosRepository;
import io.bootify.hotel_benidorm.repos.ServiciosRepository;
import io.bootify.hotel_benidorm.util.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ServiciosContratadosService {

    private final ServiciosContratadosRepository serviciosContratadosRepository;
    private final ReservaRepository reservaRepository;
    private final ServiciosRepository serviciosRepository;

    public ServiciosContratadosService(
            final ServiciosContratadosRepository serviciosContratadosRepository,
            final ReservaRepository reservaRepository,
            final ServiciosRepository serviciosRepository) {
        this.serviciosContratadosRepository = serviciosContratadosRepository;
        this.reservaRepository = reservaRepository;
        this.serviciosRepository = serviciosRepository;
    }

    public List<ServiciosContratadosDTO> findAll() {
        final List<ServiciosContratados> serviciosContratadoses = serviciosContratadosRepository.findAll(Sort.by("idServicioContratado"));
        return serviciosContratadoses.stream()
                .map(serviciosContratados -> mapToDTO(serviciosContratados, new ServiciosContratadosDTO()))
                .toList();
    }

    public ServiciosContratadosDTO get(final Integer idServicioContratado) {
        return serviciosContratadosRepository.findById(idServicioContratado)
                .map(serviciosContratados -> mapToDTO(serviciosContratados, new ServiciosContratadosDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ServiciosContratadosDTO serviciosContratadosDTO) {
        final ServiciosContratados serviciosContratados = new ServiciosContratados();
        mapToEntity(serviciosContratadosDTO, serviciosContratados);
        return serviciosContratadosRepository.save(serviciosContratados).getIdServicioContratado();
    }

    public void update(final Integer idServicioContratado,
            final ServiciosContratadosDTO serviciosContratadosDTO) {
        final ServiciosContratados serviciosContratados = serviciosContratadosRepository.findById(idServicioContratado)
                .orElseThrow(NotFoundException::new);
        mapToEntity(serviciosContratadosDTO, serviciosContratados);
        serviciosContratadosRepository.save(serviciosContratados);
    }

    public void delete(final Integer idServicioContratado) {
        serviciosContratadosRepository.deleteById(idServicioContratado);
    }

    private ServiciosContratadosDTO mapToDTO(final ServiciosContratados serviciosContratados,
            final ServiciosContratadosDTO serviciosContratadosDTO) {
        serviciosContratadosDTO.setIdServicioContratado(serviciosContratados.getIdServicioContratado());
        serviciosContratadosDTO.setReserva(serviciosContratados.getReserva() == null ? null : serviciosContratados.getReserva().getIdReserva());
        serviciosContratadosDTO.setServicio(serviciosContratados.getServicio() == null ? null : serviciosContratados.getServicio().getIdServicio());
        return serviciosContratadosDTO;
    }

    private ServiciosContratados mapToEntity(final ServiciosContratadosDTO serviciosContratadosDTO,
            final ServiciosContratados serviciosContratados) {
        final Reserva reserva = serviciosContratadosDTO.getReserva() == null ? null : reservaRepository.findById(serviciosContratadosDTO.getReserva())
                .orElseThrow(() -> new NotFoundException("reserva not found"));
        serviciosContratados.setReserva(reserva);
        final Servicios servicio = serviciosContratadosDTO.getServicio() == null ? null : serviciosRepository.findById(serviciosContratadosDTO.getServicio())
                .orElseThrow(() -> new NotFoundException("servicio not found"));
        serviciosContratados.setServicio(servicio);
        return serviciosContratados;
    }

}
