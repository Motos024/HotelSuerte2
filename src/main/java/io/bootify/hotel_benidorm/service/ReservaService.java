package io.bootify.hotel_benidorm.service;

import io.bootify.hotel_benidorm.domain.Descuento;
import io.bootify.hotel_benidorm.domain.Habitacion;
import io.bootify.hotel_benidorm.domain.Reserva;
import io.bootify.hotel_benidorm.domain.ServiciosContratados;
import io.bootify.hotel_benidorm.domain.Usuario;
import io.bootify.hotel_benidorm.model.ReservaDTO;
import io.bootify.hotel_benidorm.repos.DescuentoRepository;
import io.bootify.hotel_benidorm.repos.HabitacionRepository;
import io.bootify.hotel_benidorm.repos.ReservaRepository;
import io.bootify.hotel_benidorm.repos.ServiciosContratadosRepository;
import io.bootify.hotel_benidorm.repos.UsuarioRepository;
import io.bootify.hotel_benidorm.util.NotFoundException;
import io.bootify.hotel_benidorm.util.ReferencedWarning;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final HabitacionRepository habitacionRepository;
    private final DescuentoRepository descuentoRepository;
    private final ServiciosContratadosRepository serviciosContratadosRepository;

    public ReservaService(final ReservaRepository reservaRepository,
            final UsuarioRepository usuarioRepository,
            final HabitacionRepository habitacionRepository,
            final DescuentoRepository descuentoRepository,
            final ServiciosContratadosRepository serviciosContratadosRepository) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.habitacionRepository = habitacionRepository;
        this.descuentoRepository = descuentoRepository;
        this.serviciosContratadosRepository = serviciosContratadosRepository;
    }

    public List<ReservaDTO> findAll() {
        final List<Reserva> reservas = reservaRepository.findAll(Sort.by("idReserva"));
        return reservas.stream()
                .map(reserva -> mapToDTO(reserva, new ReservaDTO()))
                .toList();
    }

    public ReservaDTO get(final Integer idReserva) {
        return reservaRepository.findById(idReserva)
                .map(reserva -> mapToDTO(reserva, new ReservaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ReservaDTO reservaDTO) {
        final Reserva reserva = new Reserva();
        mapToEntity(reservaDTO, reserva);
        return reservaRepository.save(reserva).getIdReserva();
    }

    public void update(final Integer idReserva, final ReservaDTO reservaDTO) {
        final Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(NotFoundException::new);
        mapToEntity(reservaDTO, reserva);
        reservaRepository.save(reserva);
    }

    public void delete(final Integer idReserva) {
        reservaRepository.deleteById(idReserva);
    }

    private ReservaDTO mapToDTO(final Reserva reserva, final ReservaDTO reservaDTO) {
        reservaDTO.setIdReserva(reserva.getIdReserva());
        reservaDTO.setPrecioFinal(reserva.getPrecioFinal());
        reservaDTO.setAdulto(reserva.getAdulto());
        reservaDTO.setNinos(reserva.getNinos());
        reservaDTO.setFechaInicio(reserva.getFechaInicio());
        reservaDTO.setFechaFin(reserva.getFechaFin());
        reservaDTO.setUsuario(reserva.getUsuario() == null ? null : reserva.getUsuario().getIdUsuario());
        reservaDTO.setHabitacion(reserva.getHabitacion() == null ? null : reserva.getHabitacion().getIdHabitacion());
        reservaDTO.setDescuento(reserva.getDescuento() == null ? null : reserva.getDescuento().getIdDescuento());
        return reservaDTO;
    }

    private Reserva mapToEntity(final ReservaDTO reservaDTO, final Reserva reserva) {
        reserva.setPrecioFinal(reservaDTO.getPrecioFinal());
        reserva.setAdulto(reservaDTO.getAdulto());
        reserva.setNinos(reservaDTO.getNinos());
        reserva.setFechaInicio(reservaDTO.getFechaInicio());
        reserva.setFechaFin(reservaDTO.getFechaFin());
        final Usuario usuario = reservaDTO.getUsuario() == null ? null : usuarioRepository.findById(reservaDTO.getUsuario())
                .orElseThrow(() -> new NotFoundException("usuario not found"));
        reserva.setUsuario(usuario);
        final Habitacion habitacion = reservaDTO.getHabitacion() == null ? null : habitacionRepository.findById(reservaDTO.getHabitacion())
                .orElseThrow(() -> new NotFoundException("habitacion not found"));
        reserva.setHabitacion(habitacion);
        final Descuento descuento = reservaDTO.getDescuento() == null ? null : descuentoRepository.findById(reservaDTO.getDescuento())
                .orElseThrow(() -> new NotFoundException("descuento not found"));
        reserva.setDescuento(descuento);
        return reserva;
    }

    public ReferencedWarning getReferencedWarning(final Integer idReserva) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(NotFoundException::new);
        final ServiciosContratados reservaServiciosContratados = serviciosContratadosRepository.findFirstByReserva(reserva);
        if (reservaServiciosContratados != null) {
            referencedWarning.setKey("reserva.serviciosContratados.reserva.referenced");
            referencedWarning.addParam(reservaServiciosContratados.getIdServicioContratado());
            return referencedWarning;
        }
        return null;
    }

    public static String obtenerNombreUsuarioActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return authentication.getName(); // o getPrincipal(), dependiendo de cómo estés manejando los detalles del usuario
    }

   public ReservaDTO prereserva(ReservaDTO reservaDTO) {
        String email = obtenerNombreUsuarioActual();
        Integer idUsuario = usuarioRepository.findIdByEmail(email);
        reservaDTO.setUsuario(idUsuario);
        return reservaDTO;
    }



    public List<ReservaDTO> findReservasByUsuarioId(Integer usuarioId) {
        List<Reserva> reservas = reservaRepository.findAll();
        // Aquí filtramos manualmente, idealmente deberías hacer esto a través de una consulta personalizada en el repositorio para mayor eficiencia
        return reservas.stream()
                .filter(reserva -> reserva.getUsuario() != null && reserva.getUsuario().getIdUsuario().equals(usuarioId))
                .map(this::convertirAReservaDTO)
                .collect(Collectors.toList());
    }

    private ReservaDTO convertirAReservaDTO(Reserva reserva) {
        // Implementa la lógica de conversión aquí
        ReservaDTO dto = new ReservaDTO();
        dto.setIdReserva(reserva.getIdReserva());
        dto.setPrecioFinal(reserva.getPrecioFinal());
        dto.setAdulto(reserva.getAdulto());
        dto.setNinos(reserva.getNinos());
        dto.setFechaInicio(reserva.getFechaInicio());
        dto.setFechaFin(reserva.getFechaFin());
        // set otros campos necesarios
        return dto;
    }



}
