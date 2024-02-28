package io.bootify.hotel_benidorm.service;

import io.bootify.hotel_benidorm.domain.CaracteristicasHabitacion;
import io.bootify.hotel_benidorm.domain.Habitacion;
import io.bootify.hotel_benidorm.model.CaracteristicasHabitacionDTO;
import io.bootify.hotel_benidorm.repos.CaracteristicasHabitacionRepository;
import io.bootify.hotel_benidorm.repos.HabitacionRepository;
import io.bootify.hotel_benidorm.util.NotFoundException;
import io.bootify.hotel_benidorm.util.ReferencedWarning;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CaracteristicasHabitacionService {

    private final CaracteristicasHabitacionRepository caracteristicasHabitacionRepository;
    private final HabitacionRepository habitacionRepository;

    public CaracteristicasHabitacionService(
            final CaracteristicasHabitacionRepository caracteristicasHabitacionRepository,
            final HabitacionRepository habitacionRepository) {
        this.caracteristicasHabitacionRepository = caracteristicasHabitacionRepository;
        this.habitacionRepository = habitacionRepository;
    }

    public List<CaracteristicasHabitacionDTO> findAll() {
        final List<CaracteristicasHabitacion> caracteristicasHabitacions = caracteristicasHabitacionRepository.findAll(Sort.by("idCaracteristica"));
        return caracteristicasHabitacions.stream()
                .map(caracteristicasHabitacion -> mapToDTO(caracteristicasHabitacion, new CaracteristicasHabitacionDTO()))
                .toList();
    }

    public CaracteristicasHabitacionDTO get(final Integer idCaracteristica) {
        return caracteristicasHabitacionRepository.findById(idCaracteristica)
                .map(caracteristicasHabitacion -> mapToDTO(caracteristicasHabitacion, new CaracteristicasHabitacionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final CaracteristicasHabitacionDTO caracteristicasHabitacionDTO) {
        final CaracteristicasHabitacion caracteristicasHabitacion = new CaracteristicasHabitacion();
        mapToEntity(caracteristicasHabitacionDTO, caracteristicasHabitacion);
        return caracteristicasHabitacionRepository.save(caracteristicasHabitacion).getIdCaracteristica();
    }

    public void update(final Integer idCaracteristica,
            final CaracteristicasHabitacionDTO caracteristicasHabitacionDTO) {
        final CaracteristicasHabitacion caracteristicasHabitacion = caracteristicasHabitacionRepository.findById(idCaracteristica)
                .orElseThrow(NotFoundException::new);
        mapToEntity(caracteristicasHabitacionDTO, caracteristicasHabitacion);
        caracteristicasHabitacionRepository.save(caracteristicasHabitacion);
    }

    public void delete(final Integer idCaracteristica) {
        caracteristicasHabitacionRepository.deleteById(idCaracteristica);
    }

    private CaracteristicasHabitacionDTO mapToDTO(
            final CaracteristicasHabitacion caracteristicasHabitacion,
            final CaracteristicasHabitacionDTO caracteristicasHabitacionDTO) {
        caracteristicasHabitacionDTO.setIdCaracteristica(caracteristicasHabitacion.getIdCaracteristica());
        caracteristicasHabitacionDTO.setTipo(caracteristicasHabitacion.getTipo());
        caracteristicasHabitacionDTO.setPrecio(caracteristicasHabitacion.getPrecio());
        return caracteristicasHabitacionDTO;
    }

    private CaracteristicasHabitacion mapToEntity(
            final CaracteristicasHabitacionDTO caracteristicasHabitacionDTO,
            final CaracteristicasHabitacion caracteristicasHabitacion) {
        caracteristicasHabitacion.setTipo(caracteristicasHabitacionDTO.getTipo());
        caracteristicasHabitacion.setPrecio(caracteristicasHabitacionDTO.getPrecio());
        return caracteristicasHabitacion;
    }

    public ReferencedWarning getReferencedWarning(final Integer idCaracteristica) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final CaracteristicasHabitacion caracteristicasHabitacion = caracteristicasHabitacionRepository.findById(idCaracteristica)
                .orElseThrow(NotFoundException::new);
        final Habitacion caracteristicaHabitacion = habitacionRepository.findFirstByCaracteristica(caracteristicasHabitacion);
        if (caracteristicaHabitacion != null) {
            referencedWarning.setKey("caracteristicasHabitacion.habitacion.caracteristica.referenced");
            referencedWarning.addParam(caracteristicaHabitacion.getIdHabitacion());
            return referencedWarning;
        }
        return null;
    }



    public List<CaracteristicasHabitacionDTO> total_tipos_habitacion() {
        return caracteristicasHabitacionRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CaracteristicasHabitacionDTO convertToDto(CaracteristicasHabitacion caracteristicasHabitacion) {
        // Implementar la lógica para convertir la entidad a DTO
        CaracteristicasHabitacionDTO dto = new CaracteristicasHabitacionDTO();
        // Rellenar dto con los datos de caracteristicasHabitacion
        return dto;
    }
}
