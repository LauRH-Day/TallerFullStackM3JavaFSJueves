
package com.devsenior.llavemaestra.service;

import com.devsenior.llavemaestra.dto.AlquilerRequest;
import com.devsenior.llavemaestra.dto.AlquilerResponse;
import com.devsenior.llavemaestra.model.EstadoAlquiler;
import java.util.List;

public interface AlquilerService {

    AlquilerResponse crear(AlquilerRequest request);

    List<AlquilerResponse> listarMios();

    AlquilerResponse cancelar(Long id);
    // Consultar todos los alquileres o filtrarlos por estado
    List<AlquilerResponse> listarTodos(EstadoAlquiler estado);

    // Entregar una herramienta al cliente
    AlquilerResponse entregar(Long id);

    // Recibir la devolución de una herramienta
    AlquilerResponse devolver(Long id);


}
