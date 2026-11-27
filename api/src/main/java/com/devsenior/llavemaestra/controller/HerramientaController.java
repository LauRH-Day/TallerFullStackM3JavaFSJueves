
package com.devsenior.llavemaestra.controller;

import com.devsenior.llavemaestra.dto.EstadoRequest;
import com.devsenior.llavemaestra.dto.HerramientaResponse;
import com.devsenior.llavemaestra.service.HerramientaService;
import com.devsenior.llavemaestra.dto.HerramientaRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
@Tag(
        name = "Herramientas",
        description = "Consultar, crear, actualizar y administrar herramientas"
)
@RestController
@RequestMapping("/api/herramientas")
public class HerramientaController {

    private final HerramientaService herramientaService;

    public HerramientaController(HerramientaService herramientaService) {
        this.herramientaService = herramientaService;
    }
    @Operation(summary = "Listar herramientas")
    @GetMapping
    @PreAuthorize("hasAuthority('HERRAMIENTAS_VER')")
    public List<HerramientaResponse> listar() {
        return herramientaService.listarHerramientas();
    }

    @Operation(summary = "Consultar herramienta por ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('HERRAMIENTAS_VER')")
    public ResponseEntity<HerramientaResponse> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(herramientaService.obtenerPorId(id));
    }

    @Operation(summary = "Cambiar el estado de una herramienta")
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAuthority('HERRAMIENTAS_GESTIONAR')")
    public ResponseEntity<HerramientaResponse> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoRequest body) {

        HerramientaResponse actualizada =
                herramientaService.cambiarEstado(id, body.activo());

        return ResponseEntity.ok(actualizada);
    }


    @Operation(summary = "Registrar una herramienta")
    @PostMapping
    @PreAuthorize("hasAuthority('HERRAMIENTAS_GESTIONAR')")
    public ResponseEntity<HerramientaResponse> crear(
            @Valid @RequestBody HerramientaRequest request) {

        HerramientaResponse creada = herramientaService.crear(request);

        return ResponseEntity.status(201).body(creada);
    }

    @Operation(summary = "Actualizar una herramienta")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('HERRAMIENTAS_GESTIONAR')")
    public ResponseEntity<HerramientaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody HerramientaRequest request) {

        HerramientaResponse actualizada =
                herramientaService.actualizar(id, request);

        return ResponseEntity.ok(actualizada);
    }

}
