
package com.devsenior.llavemaestra.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;

public record EstadoRequest(
        @JsonAlias("activa")
        @NotNull(message = "Indica si queda activo (true) o inactivo (false)")
        Boolean activo
) {
}
