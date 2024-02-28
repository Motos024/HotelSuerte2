package io.bootify.hotel_benidorm.rest;

import io.bootify.hotel_benidorm.model.DescuentoDTO;
import io.bootify.hotel_benidorm.service.DescuentoService;
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
@RequestMapping(value = "/api/descuentos", produces = MediaType.APPLICATION_JSON_VALUE)
public class DescuentoResource {

    private final DescuentoService descuentoService;

    public DescuentoResource(final DescuentoService descuentoService) {
        this.descuentoService = descuentoService;
    }

    @GetMapping
    public ResponseEntity<List<DescuentoDTO>> getAllDescuentos() {
        return ResponseEntity.ok(descuentoService.findAll());
    }

    @GetMapping("/{idDescuento}")
    public ResponseEntity<DescuentoDTO> getDescuento(
            @PathVariable(name = "idDescuento") final Integer idDescuento) {
        return ResponseEntity.ok(descuentoService.get(idDescuento));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createDescuento(
            @RequestBody @Valid final DescuentoDTO descuentoDTO) {
        final Integer createdIdDescuento = descuentoService.create(descuentoDTO);
        return new ResponseEntity<>(createdIdDescuento, HttpStatus.CREATED);
    }

    @PutMapping("/{idDescuento}")
    public ResponseEntity<Integer> updateDescuento(
            @PathVariable(name = "idDescuento") final Integer idDescuento,
            @RequestBody @Valid final DescuentoDTO descuentoDTO) {
        descuentoService.update(idDescuento, descuentoDTO);
        return ResponseEntity.ok(idDescuento);
    }

    @DeleteMapping("/{idDescuento}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteDescuento(
            @PathVariable(name = "idDescuento") final Integer idDescuento) {
        final ReferencedWarning referencedWarning = descuentoService.getReferencedWarning(idDescuento);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        descuentoService.delete(idDescuento);
        return ResponseEntity.noContent().build();
    }

}
