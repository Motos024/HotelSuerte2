package io.bootify.hotel_benidorm.rest;

import io.bootify.hotel_benidorm.model.HabitacionDTO;
import io.bootify.hotel_benidorm.service.HabitacionService;
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
@RequestMapping(value = "/api/habitacions", produces = MediaType.APPLICATION_JSON_VALUE)
public class HabitacionResource {

    private final HabitacionService habitacionService;

    public HabitacionResource(final HabitacionService habitacionService) {
        this.habitacionService = habitacionService;
    }

    @GetMapping
    public ResponseEntity<List<HabitacionDTO>> getAllHabitacions() {
        return ResponseEntity.ok(habitacionService.findAll());
    }

    @GetMapping("/{idHabitacion}")
    public ResponseEntity<HabitacionDTO> getHabitacion(
            @PathVariable(name = "idHabitacion") final Integer idHabitacion) {
        return ResponseEntity.ok(habitacionService.get(idHabitacion));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createHabitacion(
            @RequestBody @Valid final HabitacionDTO habitacionDTO) {
        final Integer createdIdHabitacion = habitacionService.create(habitacionDTO);
        return new ResponseEntity<>(createdIdHabitacion, HttpStatus.CREATED);
    }

    @PutMapping("/{idHabitacion}")
    public ResponseEntity<Integer> updateHabitacion(
            @PathVariable(name = "idHabitacion") final Integer idHabitacion,
            @RequestBody @Valid final HabitacionDTO habitacionDTO) {
        habitacionService.update(idHabitacion, habitacionDTO);
        return ResponseEntity.ok(idHabitacion);
    }

    @DeleteMapping("/{idHabitacion}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteHabitacion(
            @PathVariable(name = "idHabitacion") final Integer idHabitacion) {
        final ReferencedWarning referencedWarning = habitacionService.getReferencedWarning(idHabitacion);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        habitacionService.delete(idHabitacion);
        return ResponseEntity.noContent().build();
    }

}
