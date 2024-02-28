package io.bootify.hotel_benidorm.rest;

import io.bootify.hotel_benidorm.model.ReservaDTO;
import io.bootify.hotel_benidorm.service.ReservaService;
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
@RequestMapping(value = "/api/reservas", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservaResource {

    private final ReservaService reservaService;

    public ReservaResource(final ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public ResponseEntity<List<ReservaDTO>> getAllReservas() {
        return ResponseEntity.ok(reservaService.findAll());
    }

    @GetMapping("/{idReserva}")
    public ResponseEntity<ReservaDTO> getReserva(
            @PathVariable(name = "idReserva") final Integer idReserva) {
        return ResponseEntity.ok(reservaService.get(idReserva));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createReserva(@RequestBody @Valid final ReservaDTO reservaDTO) {
        final Integer createdIdReserva = reservaService.create(reservaDTO);
        return new ResponseEntity<>(createdIdReserva, HttpStatus.CREATED);
    }

    @PutMapping("/{idReserva}")
    public ResponseEntity<Integer> updateReserva(
            @PathVariable(name = "idReserva") final Integer idReserva,
            @RequestBody @Valid final ReservaDTO reservaDTO) {
        reservaService.update(idReserva, reservaDTO);
        return ResponseEntity.ok(idReserva);
    }

    @DeleteMapping("/{idReserva}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteReserva(
            @PathVariable(name = "idReserva") final Integer idReserva) {
        final ReferencedWarning referencedWarning = reservaService.getReferencedWarning(idReserva);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        reservaService.delete(idReserva);
        return ResponseEntity.noContent().build();
    }

}
