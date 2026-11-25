
package com.devsenior.llavemaestra.controller;

import com.devsenior.llavemaestra.dto.AlquilerRequest;
import com.devsenior.llavemaestra.dto.AlquilerResponse;
import com.devsenior.llavemaestra.service.AlquilerService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alquileres")
public class AlquilerController {

    private final AlquilerService alquilerService;

    public AlquilerController(AlquilerService alquilerService) {
        this.alquilerService = alquilerService;
    }

    // Crear un alquiler
    @PostMapping
    @PreAuthorize("hasAuthority('ALQUILERES_CREAR')")
    public ResponseEntity<AlquilerResponse> crear(
            @Valid @RequestBody AlquilerRequest request) {

        AlquilerResponse creado = alquilerService.crear(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // Consultar únicamente los alquileres del usuario autenticado
    @GetMapping("/mios")
    @PreAuthorize("hasAuthority('ALQUILERES_VER_PROPIOS')")
    public ResponseEntity<List<AlquilerResponse>> listarMios() {

        return ResponseEntity.ok(alquilerService.listarMios());
    }

    // Cancelar un alquiler propio o gestionado por un administrador
    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyAuthority('ALQUILERES_CANCELAR_PROPIOS', 'ALQUILERES_GESTIONAR')")
    public ResponseEntity<AlquilerResponse> cancelar(
            @PathVariable Long id) {

        return ResponseEntity.ok(alquilerService.cancelar(id));
    }
}
