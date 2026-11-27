package com.devsenior.llavemaestra.service;

import com.devsenior.llavemaestra.dto.HerramientaRequest;
import com.devsenior.llavemaestra.dto.HerramientaResponse;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;

public interface HerramientaService {
        List<HerramientaResponse> listarHerramientas();
        HerramientaResponse obtenerPorId(Long id);
        HerramientaResponse cambiarEstado(Long id, boolean activa);
        HerramientaResponse crear(HerramientaRequest request);
        HerramientaResponse actualizar(Long id, HerramientaRequest request);
    }

