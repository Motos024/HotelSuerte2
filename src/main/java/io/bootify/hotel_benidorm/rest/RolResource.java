package io.bootify.hotel_benidorm.rest;

import io.bootify.hotel_benidorm.model.RolDTO;
import io.bootify.hotel_benidorm.service.RolService;
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
@RequestMapping(value = "/api/rols", produces = MediaType.APPLICATION_JSON_VALUE)
public class RolResource {

    private final RolService rolService;

    public RolResource(final RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public ResponseEntity<List<RolDTO>> getAllRols() {
        return ResponseEntity.ok(rolService.findAll());
    }

    @GetMapping("/{idRol}")
    public ResponseEntity<RolDTO> getRol(@PathVariable(name = "idRol") final Integer idRol) {
        return ResponseEntity.ok(rolService.get(idRol));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createRol(@RequestBody @Valid final RolDTO rolDTO) {
        final Integer createdIdRol = rolService.create(rolDTO);
        return new ResponseEntity<>(createdIdRol, HttpStatus.CREATED);
    }

    @PutMapping("/{idRol}")
    public ResponseEntity<Integer> updateRol(@PathVariable(name = "idRol") final Integer idRol,
            @RequestBody @Valid final RolDTO rolDTO) {
        rolService.update(idRol, rolDTO);
        return ResponseEntity.ok(idRol);
    }

    @DeleteMapping("/{idRol}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRol(@PathVariable(name = "idRol") final Integer idRol) {
        final ReferencedWarning referencedWarning = rolService.getReferencedWarning(idRol);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        rolService.delete(idRol);
        return ResponseEntity.noContent().build();
    }

}
