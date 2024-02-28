package io.bootify.hotel_benidorm.service;

import io.bootify.hotel_benidorm.domain.CaracteristicasHabitacion;
import io.bootify.hotel_benidorm.domain.Habitacion;
import io.bootify.hotel_benidorm.domain.Reserva;
import io.bootify.hotel_benidorm.model.HabitacionDTO;
import io.bootify.hotel_benidorm.repos.CaracteristicasHabitacionRepository;
import io.bootify.hotel_benidorm.repos.HabitacionRepository;
import io.bootify.hotel_benidorm.repos.ReservaRepository;
import io.bootify.hotel_benidorm.util.NotFoundException;
import io.bootify.hotel_benidorm.util.ReferencedWarning;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class HabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final CaracteristicasHabitacionRepository caracteristicasHabitacionRepository;
    private final ReservaRepository reservaRepository;

    public HabitacionService(final HabitacionRepository habitacionRepository,
            final CaracteristicasHabitacionRepository caracteristicasHabitacionRepository,
            final ReservaRepository reservaRepository) {
        this.habitacionRepository = habitacionRepository;
        this.caracteristicasHabitacionRepository = caracteristicasHabitacionRepository;
        this.reservaRepository = reservaRepository;
    }

    public List<HabitacionDTO> findAll() {
        final List<Habitacion> habitacions = habitacionRepository.findAll(Sort.by("idHabitacion"));
        return habitacions.stream()
                .map(habitacion -> mapToDTO(habitacion, new HabitacionDTO()))
                .toList();
    }

    public HabitacionDTO get(final Integer idHabitacion) {
        return habitacionRepository.findById(idHabitacion)
                .map(habitacion -> mapToDTO(habitacion, new HabitacionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final HabitacionDTO habitacionDTO) {
        final Habitacion habitacion = new Habitacion();
        mapToEntity(habitacionDTO, habitacion);
        return habitacionRepository.save(habitacion).getIdHabitacion();
    }

    public void update(final Integer idHabitacion, final HabitacionDTO habitacionDTO) {
        final Habitacion habitacion = habitacionRepository.findById(idHabitacion)
                .orElseThrow(NotFoundException::new);
        mapToEntity(habitacionDTO, habitacion);
        habitacionRepository.save(habitacion);
    }

    public void delete(final Integer idHabitacion) {
        habitacionRepository.deleteById(idHabitacion);
    }

    private HabitacionDTO mapToDTO(final Habitacion habitacion, final HabitacionDTO habitacionDTO) {
        habitacionDTO.setIdHabitacion(habitacion.getIdHabitacion());
        habitacionDTO.setPlanta(habitacion.getPlanta());
        habitacionDTO.setCaracteristica(habitacion.getCaracteristica() == null ? null : habitacion.getCaracteristica().getIdCaracteristica());
        return habitacionDTO;
    }

    private Habitacion mapToEntity(final HabitacionDTO habitacionDTO, final Habitacion habitacion) {
        habitacion.setPlanta(habitacionDTO.getPlanta());
        final CaracteristicasHabitacion caracteristica = habitacionDTO.getCaracteristica() == null ? null : caracteristicasHabitacionRepository.findById(habitacionDTO.getCaracteristica())
                .orElseThrow(() -> new NotFoundException("caracteristica not found"));
        habitacion.setCaracteristica(caracteristica);
        return habitacion;
    }

    public ReferencedWarning getReferencedWarning(final Integer idHabitacion) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Habitacion habitacion = habitacionRepository.findById(idHabitacion)
                .orElseThrow(NotFoundException::new);
        final Reserva habitacionReserva = reservaRepository.findFirstByHabitacion(habitacion);
        if (habitacionReserva != null) {
            referencedWarning.setKey("habitacion.reserva.habitacion.referenced");
            referencedWarning.addParam(habitacionReserva.getIdReserva());
            return referencedWarning;
        }
        return null;
    }

    public List<Object[]> findHabitacionesDisponibles(Integer idCaracteristica, Date fechaInicio, Date fechaFin) {
        return habitacionRepository.findHabitacionesDisponiblesPorCaracteristicaNative(idCaracteristica, fechaInicio, fechaFin);
    }


}
