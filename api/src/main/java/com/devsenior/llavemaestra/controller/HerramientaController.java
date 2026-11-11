
package com.devsenior.llavemaestra.controller;

import com.devsenior.llavemaestra.dto.EstadoRequest;
import com.devsenior.llavemaestra.dto.HerramientaResponse;
import com.devsenior.llavemaestra.service.HerramientaService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/herramientas")
public class HerramientaController {

    private final HerramientaService herramientaService;

    public HerramientaController(HerramientaService herramientaService) {
        this.herramientaService = herramientaService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('HERRAMIENTAS_VER')")
    public List<HerramientaResponse> listar() {
        return herramientaService.listarHerramientas();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('HERRAMIENTAS_VER')")
    public ResponseEntity<HerramientaResponse> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(herramientaService.obtenerPorId(id));
    }

@PatchMapping("/{id}/estado")
@PreAuthorize("hasAuthority('HERRAMIENTAS_GESTIONAR')")
public ResponseEntity<HerramientaResponse> cambiarEstado(
        @PathVariable Long id,
        @Valid @RequestBody EstadoRequest body) {

    HerramientaResponse actualizada =
            herramientaService.cambiarEstado(id, body.activo());

    return ResponseEntity.ok(actualizada);
}
}
