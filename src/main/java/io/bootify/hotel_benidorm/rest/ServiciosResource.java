package io.bootify.hotel_benidorm.rest;

import io.bootify.hotel_benidorm.model.ServiciosDTO;
import io.bootify.hotel_benidorm.service.ServiciosService;
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
@RequestMapping(value = "/api/servicioss", produces = MediaType.APPLICATION_JSON_VALUE)
public class ServiciosResource {

    private final ServiciosService serviciosService;

    public ServiciosResource(final ServiciosService serviciosService) {
        this.serviciosService = serviciosService;
    }

    @GetMapping
    public ResponseEntity<List<ServiciosDTO>> getAllServicioss() {
        return ResponseEntity.ok(serviciosService.findAll());
    }

    @GetMapping("/{idServicio}")
    public ResponseEntity<ServiciosDTO> getServicios(
            @PathVariable(name = "idServicio") final Integer idServicio) {
        return ResponseEntity.ok(serviciosService.get(idServicio));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createServicios(
            @RequestBody @Valid final ServiciosDTO serviciosDTO) {
        final Integer createdIdServicio = serviciosService.create(serviciosDTO);
        return new ResponseEntity<>(createdIdServicio, HttpStatus.CREATED);
    }

    @PutMapping("/{idServicio}")
    public ResponseEntity<Integer> updateServicios(
            @PathVariable(name = "idServicio") final Integer idServicio,
            @RequestBody @Valid final ServiciosDTO serviciosDTO) {
        serviciosService.update(idServicio, serviciosDTO);
        return ResponseEntity.ok(idServicio);
    }

    @DeleteMapping("/{idServicio}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteServicios(
            @PathVariable(name = "idServicio") final Integer idServicio) {
        final ReferencedWarning referencedWarning = serviciosService.getReferencedWarning(idServicio);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        serviciosService.delete(idServicio);
        return ResponseEntity.noContent().build();
    }

}
