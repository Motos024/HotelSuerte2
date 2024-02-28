package io.bootify.hotel_benidorm.rest;

import io.bootify.hotel_benidorm.model.CaracteristicasHabitacionDTO;
import io.bootify.hotel_benidorm.service.CaracteristicasHabitacionService;
import io.bootify.hotel_benidorm.util.ReferencedException;
import io.bootify.hotel_benidorm.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/caracteristicasHabitacions", produces = MediaType.APPLICATION_JSON_VALUE)
public class CaracteristicasHabitacionResource {

    private final CaracteristicasHabitacionService caracteristicasHabitacionService;

    public CaracteristicasHabitacionResource(
            final CaracteristicasHabitacionService caracteristicasHabitacionService) {
        this.caracteristicasHabitacionService = caracteristicasHabitacionService;
    }

    @GetMapping
    public ResponseEntity<List<CaracteristicasHabitacionDTO>> getAllCaracteristicasHabitacions() {
        return ResponseEntity.ok(caracteristicasHabitacionService.findAll());
    }

    @GetMapping("/{idCaracteristica}")
    public ResponseEntity<CaracteristicasHabitacionDTO> getCaracteristicasHabitacion(
            @PathVariable(name = "idCaracteristica") final Integer idCaracteristica) {
        return ResponseEntity.ok(caracteristicasHabitacionService.get(idCaracteristica));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createCaracteristicasHabitacion(
            @RequestBody @Valid final CaracteristicasHabitacionDTO caracteristicasHabitacionDTO) {
        final Integer createdIdCaracteristica = caracteristicasHabitacionService.create(caracteristicasHabitacionDTO);
        return new ResponseEntity<>(createdIdCaracteristica, HttpStatus.CREATED);
    }

    @PutMapping("/{idCaracteristica}")
    public ResponseEntity<Integer> updateCaracteristicasHabitacion(
            @PathVariable(name = "idCaracteristica") final Integer idCaracteristica,
            @RequestBody @Valid final CaracteristicasHabitacionDTO caracteristicasHabitacionDTO) {
        caracteristicasHabitacionService.update(idCaracteristica, caracteristicasHabitacionDTO);
        return ResponseEntity.ok(idCaracteristica);
    }

    @DeleteMapping("/{idCaracteristica}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCaracteristicasHabitacion(
            @PathVariable(name = "idCaracteristica") final Integer idCaracteristica) {
        final ReferencedWarning referencedWarning = caracteristicasHabitacionService.getReferencedWarning(idCaracteristica);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        caracteristicasHabitacionService.delete(idCaracteristica);
        return ResponseEntity.noContent().build();
    }

}
