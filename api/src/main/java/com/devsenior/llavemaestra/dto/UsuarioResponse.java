package com.devsenior.llavemaestra.dto;

import com.devsenior.llavemaestra.model.Rol;

import java.time.LocalDateTime;

// Nunca expone la contraseña
public record UsuarioResponse(
        Long id,
        String nombre,
        String email,
        Rol rol,
        boolean activo,
        LocalDateTime creadoEn
) {
}