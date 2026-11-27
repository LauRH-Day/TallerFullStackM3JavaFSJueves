package com.devsenior.llavemaestra.service;

import com.devsenior.llavemaestra.dto.AlquilerResponse;
import com.devsenior.llavemaestra.dto.HerramientaAlquiladaResponse;
import com.devsenior.llavemaestra.dto.ResumenResponse;
import com.devsenior.llavemaestra.model.Alquiler;
import com.devsenior.llavemaestra.model.EstadoAlquiler;
import com.devsenior.llavemaestra.repository.AlquilerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final AlquilerRepository alquilerRepository;

    public ReporteServiceImpl(AlquilerRepository alquilerRepository) {
        this.alquilerRepository = alquilerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ResumenResponse obtenerResumen() {

        LocalDate hoy = LocalDate.now();
        LocalDateTime inicioMes = hoy.withDayOfMonth(1).atStartOfDay();

        long ingresosMes = alquilerRepository.sumarIngresosMes(
                inicioMes,
                EstadoAlquiler.CANCELADO
        );

        long alquileresActivos = alquilerRepository.countByEstadoIn(
                List.of(EstadoAlquiler.RESERVADO, EstadoAlquiler.ENTREGADO)
        );

        List<AlquilerResponse> vencidos = alquilerRepository.buscarVencidos(
                EstadoAlquiler.ENTREGADO,
                hoy
        ).stream().map(this::convertirRespuesta).toList();

        List<HerramientaAlquiladaResponse> masAlquiladas =
                alquilerRepository.contarAlquileresPorHerramienta(
                                EstadoAlquiler.CANCELADO
                        ).stream()
                        .map(fila -> new HerramientaAlquiladaResponse(
                                ((com.devsenior.llavemaestra.model.Herramienta) fila[0]).getNombre(),
                                ((Number) fila[1]).longValue()
                        ))
                        .toList();

        return new ResumenResponse(
                ingresosMes,
                alquileresActivos,
                vencidos,
                masAlquiladas
        );
    }

    private AlquilerResponse convertirRespuesta(Alquiler a) {
        return new AlquilerResponse(
                a.getId(),
                a.getHerramienta().getId(),
                a.getHerramienta().getNombre(),
                a.getCliente().getId(),
                a.getCliente().getNombre(),
                a.getFechaInicio(),
                a.getFechaFin(),
                a.getDias(),
                a.getValorTotal(),
                a.getDeposito(),
                a.getEstado(),
                a.getCreadoEn()
        );
    }
}