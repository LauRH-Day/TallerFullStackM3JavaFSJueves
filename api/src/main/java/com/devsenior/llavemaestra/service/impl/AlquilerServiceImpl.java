
package com.devsenior.llavemaestra.service.impl;

import com.devsenior.llavemaestra.dto.AlquilerRequest;
import com.devsenior.llavemaestra.dto.AlquilerResponse;
import com.devsenior.llavemaestra.exception.DatoInvalidoException;
import com.devsenior.llavemaestra.exception.ReglaNegocioException;
import com.devsenior.llavemaestra.exception.RecursoNoEncontradoException;
import com.devsenior.llavemaestra.model.Alquiler;
import com.devsenior.llavemaestra.model.EstadoAlquiler;
import com.devsenior.llavemaestra.model.Herramienta;
import com.devsenior.llavemaestra.model.Usuario;
import com.devsenior.llavemaestra.repository.AlquilerRepository;
import com.devsenior.llavemaestra.repository.HerramientaRepository;
import com.devsenior.llavemaestra.repository.UsuarioRepository;
import com.devsenior.llavemaestra.service.AlquilerService;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AlquilerServiceImpl implements AlquilerService {

    private static final int MAX_DIAS = 15;
    private static final int MAX_ALQUILERES_CLIENTE = 3;

    private static final List<EstadoAlquiler> ESTADOS_ACTIVOS = List.of(
            EstadoAlquiler.RESERVADO,
            EstadoAlquiler.ENTREGADO
    );

    private final AlquilerRepository alquilerRepository;
    private final HerramientaRepository herramientaRepository;
    private final UsuarioRepository usuarioRepository;

    public AlquilerServiceImpl(
            AlquilerRepository alquilerRepository,
            HerramientaRepository herramientaRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.alquilerRepository = alquilerRepository;
        this.herramientaRepository = herramientaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // --------------------------------------------------
    // CREAR ALQUILER
    // --------------------------------------------------

    @Override
    @Transactional
    public AlquilerResponse crear(AlquilerRequest request) {

        // Regla 1: datos obligatorios
        if (request == null
                || request.herramientaId() == null
                || request.fechaInicio() == null
                || request.fechaFin() == null) {
            throw new DatoInvalidoException(
                    "Debes indicar la herramienta, la fecha de inicio y la fecha de fin"
            );
        }

        // Identificar al cliente mediante el token
        Usuario usuarioAutenticado = obtenerUsuarioAutenticado();

        // Bloquear al usuario para evitar superar simultáneamente
        // el máximo de tres alquileres activos.
        Usuario cliente = usuarioRepository
                .findByIdForUpdate(usuarioAutenticado.getId())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Cliente no encontrado")
                );

        // Bloquear la herramienta para evitar sobrealquileres simultáneos.
        Herramienta herramienta = herramientaRepository
                .findByIdForUpdate(request.herramientaId())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Herramienta no encontrada")
                );

        // Regla 3: herramienta retirada
        if (!herramienta.isActiva()) {
            throw new ReglaNegocioException(
                    "Esta herramienta no está disponible para alquiler"
            );
        }

        // Regla 4: fechas invertidas
        if (request.fechaFin().isBefore(request.fechaInicio())) {
            throw new DatoInvalidoException(
                    "La fecha de fin no puede ser anterior a la fecha de inicio"
            );
        }

        // Regla 5: no permitir fechas anteriores a hoy
        LocalDate hoy = LocalDate.now();

        if (request.fechaInicio().isBefore(hoy)) {
            throw new DatoInvalidoException(
                    "La fecha de inicio no puede ser anterior a hoy"
            );
        }

        // Regla 6: máximo 15 días, contando inicio y fin
        int dias = (int) ChronoUnit.DAYS.between(
                request.fechaInicio(),
                request.fechaFin()
        ) + 1;

        if (dias > MAX_DIAS) {
            throw new DatoInvalidoException(
                    "El alquiler no puede superar los 15 días"
            );
        }

        // Regla 7: máximo tres alquileres activos por cliente
        long alquileresCliente = alquilerRepository
                .countByClienteIdAndEstadoIn(
                        cliente.getId(),
                        ESTADOS_ACTIVOS
                );

        if (alquileresCliente >= MAX_ALQUILERES_CLIENTE) {
            throw new ReglaNegocioException(
                    "Ya tienes el máximo de 3 alquileres activos"
            );
        }

        // Regla 8: disponibilidad de unidades en las fechas solicitadas
        long unidadesOcupadas = alquilerRepository.contarCruces(
                herramienta.getId(),
                request.fechaInicio(),
                request.fechaFin(),
                ESTADOS_ACTIVOS
        );

        if (unidadesOcupadas >= herramienta.getUnidades()) {
            throw new ReglaNegocioException(
                    "No hay unidades disponibles para las fechas seleccionadas"
            );
        }

        // El backend calcula todos los valores.
        Alquiler alquiler = new Alquiler();
        alquiler.setHerramienta(herramienta);
        alquiler.setCliente(cliente);
        alquiler.setFechaInicio(request.fechaInicio());
        alquiler.setFechaFin(request.fechaFin());
        alquiler.setDias(dias);
        alquiler.setValorTotal(dias * herramienta.getPrecioDia());
        alquiler.setDeposito(herramienta.getDeposito());
        alquiler.setEstado(EstadoAlquiler.RESERVADO);

        Alquiler guardado = alquilerRepository.save(alquiler);

        return convertirRespuesta(guardado);
    }

    // --------------------------------------------------
    // LISTAR MIS ALQUILERES
    // --------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public List<AlquilerResponse> listarMios() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return alquilerRepository
                .findByClienteEmailOrderByCreadoEnDesc(email)
                .stream()
                .map(this::convertirRespuesta)
                .toList();
    }

    // --------------------------------------------------
    // CANCELAR ALQUILER
    // --------------------------------------------------

    @Override
    @Transactional
    public AlquilerResponse cancelar(Long id) {

        Alquiler alquiler = alquilerRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Alquiler no encontrado")
                );

        Authentication autenticacion = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = autenticacion.getName();

        boolean puedeGestionar = autenticacion.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ALQUILERES_GESTIONAR")
                );

        // Regla 2: solo el propietario o un gestor puede cancelar
        if (!puedeGestionar
                && !alquiler.getCliente().getEmail().equals(email)) {
            throw new AccessDeniedException(
                    "No puedes cancelar un alquiler que no te pertenece"
            );
        }

        // Regla 3: solo se puede cancelar si sigue reservado
        if (alquiler.getEstado() != EstadoAlquiler.RESERVADO) {
            throw new ReglaNegocioException(
                    "Solo puedes cancelar alquileres que estén reservados"
            );
        }

        // Regla 4: no se puede cancelar si ya empezó o empieza hoy
        if (!alquiler.getFechaInicio().isAfter(LocalDate.now())) {
            throw new ReglaNegocioException(
                    "No puedes cancelar un alquiler que empieza hoy o ya comenzó"
            );
        }

        alquiler.setEstado(EstadoAlquiler.CANCELADO);

        return convertirRespuesta(alquilerRepository.save(alquiler));
    }

    // --------------------------------------------------
    // MÉTODOS AUXILIARES
    // --------------------------------------------------

    private Usuario obtenerUsuarioAutenticado() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Usuario no encontrado")
                );
    }

    private AlquilerResponse convertirRespuesta(Alquiler alquiler) {

        return new AlquilerResponse(
                alquiler.getId(),
                alquiler.getHerramienta().getId(),
                alquiler.getHerramienta().getNombre(),
                alquiler.getCliente().getId(),
                alquiler.getCliente().getNombre(),
                alquiler.getFechaInicio(),
                alquiler.getFechaFin(),
                alquiler.getDias(),
                alquiler.getValorTotal(),
                alquiler.getDeposito(),
                alquiler.getEstado(),
                alquiler.getCreadoEn()
        );
    }
}
