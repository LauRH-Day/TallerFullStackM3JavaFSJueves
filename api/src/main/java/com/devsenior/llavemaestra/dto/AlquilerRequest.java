
package com.devsenior.llavemaestra.dto;

import java.time.LocalDate;

public record AlquilerRequest(
        Long herramientaId,
        LocalDate fechaInicio,
        LocalDate fechaFin
) {}
