
package com.devsenior.llavemaestra.controller;

import com.devsenior.llavemaestra.dto.AlquilerRequest;
import com.devsenior.llavemaestra.dto.AlquilerResponse;
import com.devsenior.llavemaestra.model.EstadoAlquiler;
import com.devsenior.llavemaestra.service.AlquilerService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(
        name = "Alquileres",
        description = "Crear, consultar, cancelar, entregar y devolver alquileres"
)
@RestController
@RequestMapping("/api/alquileres")
public class AlquilerController {

    private final AlquilerService alquilerService;

    public AlquilerController(AlquilerService alquilerService) {
        this.alquilerService = alquilerService;
    }

    @Operation(summary = "Crear un alquiler")
    @PostMapping
    @PreAuthorize("hasAuthority('ALQUILERES_CREAR')")
    public ResponseEntity<AlquilerResponse> crear(
            @Valid @RequestBody AlquilerRequest request) {

        AlquilerResponse creado = alquilerService.crear(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Consultar mis alquileres")
    @GetMapping("/mios")
    @PreAuthorize("hasAuthority('ALQUILERES_VER_PROPIOS')")
    public ResponseEntity<List<AlquilerResponse>> listarMios() {

        return ResponseEntity.ok(alquilerService.listarMios());
    }

    @Operation(summary = "Consultar todos los alquileres")
    @GetMapping
    @PreAuthorize("hasAuthority('ALQUILERES_VER_TODOS')")
    public ResponseEntity<List<AlquilerResponse>> listarTodos(
            @RequestParam(required = false) EstadoAlquiler estado) {

        return ResponseEntity.ok(alquilerService.listarTodos(estado));
    }

    @Operation(summary = "Entregar una herramienta")
    @PatchMapping("/{id}/entregar")
    @PreAuthorize("hasAuthority('ALQUILERES_GESTIONAR')")
    public ResponseEntity<AlquilerResponse> entregar(@PathVariable Long id) {
        return ResponseEntity.ok(alquilerService.entregar(id));
    }

    @Operation(summary = "Recibir una herramienta devuelta")
    @PatchMapping("/{id}/devolver")
    @PreAuthorize("hasAuthority('ALQUILERES_GESTIONAR')")
    public ResponseEntity<AlquilerResponse> devolver(@PathVariable Long id) {
        return ResponseEntity.ok(alquilerService.devolver(id));
    }

    @Operation(summary = "Cancelar un alquiler")
    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyAuthority('ALQUILERES_CANCELAR_PROPIOS', 'ALQUILERES_GESTIONAR')")
    public ResponseEntity<AlquilerResponse> cancelar(
            @PathVariable Long id) {

        return ResponseEntity.ok(alquilerService.cancelar(id));
    }
}
