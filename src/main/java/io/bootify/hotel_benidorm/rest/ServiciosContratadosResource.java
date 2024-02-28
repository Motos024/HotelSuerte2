package io.bootify.hotel_benidorm.rest;

import io.bootify.hotel_benidorm.model.ServiciosContratadosDTO;
import io.bootify.hotel_benidorm.service.ServiciosContratadosService;
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
@RequestMapping(value = "/api/serviciosContratadoss", produces = MediaType.APPLICATION_JSON_VALUE)
public class ServiciosContratadosResource {

    private final ServiciosContratadosService serviciosContratadosService;

    public ServiciosContratadosResource(
            final ServiciosContratadosService serviciosContratadosService) {
        this.serviciosContratadosService = serviciosContratadosService;
    }

    @GetMapping
    public ResponseEntity<List<ServiciosContratadosDTO>> getAllServiciosContratadoss() {
        return ResponseEntity.ok(serviciosContratadosService.findAll());
    }

    @GetMapping("/{idServicioContratado}")
    public ResponseEntity<ServiciosContratadosDTO> getServiciosContratados(
            @PathVariable(name = "idServicioContratado") final Integer idServicioContratado) {
        return ResponseEntity.ok(serviciosContratadosService.get(idServicioContratado));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createServiciosContratados(
            @RequestBody @Valid final ServiciosContratadosDTO serviciosContratadosDTO) {
        final Integer createdIdServicioContratado = serviciosContratadosService.create(serviciosContratadosDTO);
        return new ResponseEntity<>(createdIdServicioContratado, HttpStatus.CREATED);
    }

    @PutMapping("/{idServicioContratado}")
    public ResponseEntity<Integer> updateServiciosContratados(
            @PathVariable(name = "idServicioContratado") final Integer idServicioContratado,
            @RequestBody @Valid final ServiciosContratadosDTO serviciosContratadosDTO) {
        serviciosContratadosService.update(idServicioContratado, serviciosContratadosDTO);
        return ResponseEntity.ok(idServicioContratado);
    }

    @DeleteMapping("/{idServicioContratado}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteServiciosContratados(
            @PathVariable(name = "idServicioContratado") final Integer idServicioContratado) {
        serviciosContratadosService.delete(idServicioContratado);
        return ResponseEntity.noContent().build();
    }

}
