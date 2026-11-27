
package com.devsenior.llavemaestra.dto;

import com.devsenior.llavemaestra.model.Categoria;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record HerramientaRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 80, message = "El nombre admite máximo 80 caracteres")
        String nombre,

        @NotNull(message = "La categoría es obligatoria")
        Categoria categoria,

        @Min(value = 1000, message = "El precio por día debe ser mínimo 1000")
        int precioDia,

        @Min(value = 0, message = "El depósito no puede ser negativo")
        int deposito,

        @Min(value = 1, message = "Debe existir al menos una unidad")
        @Max(value = 50, message = "El máximo permitido es 50 unidades")
        int unidades,

        @Size(max = 300, message = "La descripción admite máximo 300 caracteres")
        String descripcion,

        @Size(max = 300, message = "La URL de imagen admite máximo 300 caracteres")
        String imagenUrl
) {}
