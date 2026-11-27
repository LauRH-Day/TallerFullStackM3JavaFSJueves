package com.devsenior.llavemaestra.controller;

import com.devsenior.llavemaestra.dto.ResumenResponse;
import com.devsenior.llavemaestra.service.ReporteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "Reportes",
        description = "Consultar indicadores y resumen del negocio"
)
@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @Operation(summary = "Consultar resumen del negocio")
    @GetMapping("/resumen")
    @PreAuthorize("hasAuthority('REPORTES_VER')")
    public ResponseEntity<ResumenResponse> obtenerResumen() {
        return ResponseEntity.ok(reporteService.obtenerResumen());
    }
}