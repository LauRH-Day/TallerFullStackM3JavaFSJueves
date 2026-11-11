package com.devsenior.llavemaestra.dto;

import com.devsenior.llavemaestra.model.Categoria;
import java.math.BigDecimal;

public record HerramientaRequest(
        String nombre,
        Categoria categoria,
        int precioDia,
        int deposito,
        int unidades,
        boolean activa,
        String descripcion,
        String imagenUrl
) {}