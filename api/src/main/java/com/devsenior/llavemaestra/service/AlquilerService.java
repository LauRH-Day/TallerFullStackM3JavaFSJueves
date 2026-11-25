
package com.devsenior.llavemaestra.service;

import com.devsenior.llavemaestra.dto.AlquilerRequest;
import com.devsenior.llavemaestra.dto.AlquilerResponse;

import java.util.List;

public interface AlquilerService {

    AlquilerResponse crear(AlquilerRequest request);

    List<AlquilerResponse> listarMios();

    AlquilerResponse cancelar(Long id);
}
