package com.devsenior.llavemaestra.service.impl;

import com.devsenior.llavemaestra.dto.HerramientaRequest;
import com.devsenior.llavemaestra.dto.HerramientaResponse;
import com.devsenior.llavemaestra.exception.RecursoNoEncontradoException;
import com.devsenior.llavemaestra.model.Herramienta;
import com.devsenior.llavemaestra.model.Rol;
import com.devsenior.llavemaestra.model.Usuario;
import com.devsenior.llavemaestra.repository.HerramientaRepository;
import com.devsenior.llavemaestra.repository.UsuarioRepository;
import com.devsenior.llavemaestra.service.HerramientaService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.devsenior.llavemaestra.dto.HerramientaRequest;
import com.devsenior.llavemaestra.exception.ReglaNegocioException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class HerramientaServiceImpl implements HerramientaService {

    private final HerramientaRepository herramientaRepository;
    private final UsuarioRepository usuarioRepository;

    public HerramientaServiceImpl(HerramientaRepository herramientaRepository, UsuarioRepository usuarioRepository) {
        this.herramientaRepository = herramientaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<HerramientaResponse> listarHerramientas() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Herramienta> herramientas = herramientaRepository.findAll();

        if (usuario.getRol() != Rol.ADMIN) {
            herramientas = herramientas.stream().filter(Herramienta::isActiva).collect(Collectors.toList());
        }

        return herramientas.stream().map(this::mapearAEResponse).collect(Collectors.toList());
    }

    @Override
    public HerramientaResponse obtenerPorId(Long id) {
        Herramienta herramienta = herramientaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Herramienta no encontrada"));

        return mapearAEResponse(herramienta);
    }

   @Override
public HerramientaResponse cambiarEstado(Long id, boolean activa) {
    Herramienta herramienta = herramientaRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "Herramienta con id " + id + " no encontrada"
            ));

    herramienta.setActiva(activa);
    herramientaRepository.save(herramienta);

    return mapearAEResponse(herramienta);
}


    @Override
    public HerramientaResponse crear(HerramientaRequest request) {

        String nombre = request.nombre().trim();

        if (herramientaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ReglaNegocioException(
                    "Ya existe una herramienta con ese nombre"
            );
        }

        Herramienta herramienta = new Herramienta();

        herramienta.setNombre(nombre);
        herramienta.setCategoria(request.categoria());
        herramienta.setPrecioDia(request.precioDia());
        herramienta.setDeposito(request.deposito());
        herramienta.setUnidades(request.unidades());
        herramienta.setDescripcion(request.descripcion());
        herramienta.setImagenUrl(request.imagenUrl());

        // Toda herramienta nueva comienza activa.
        herramienta.setActiva(true);

        Herramienta guardada = herramientaRepository.save(herramienta);

        return mapearAEResponse(guardada);
    }

    @Override
    public HerramientaResponse actualizar(Long id, HerramientaRequest request) {

        Herramienta herramienta = herramientaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Herramienta con id " + id + " no encontrada"
                ));

        String nombre = request.nombre().trim();

        if (herramientaRepository.existsByNombreIgnoreCaseAndIdNot(nombre, id)) {
            throw new ReglaNegocioException(
                    "Ya existe otra herramienta con ese nombre"
            );
        }

        herramienta.setNombre(nombre);
        herramienta.setCategoria(request.categoria());
        herramienta.setPrecioDia(request.precioDia());
        herramienta.setDeposito(request.deposito());
        herramienta.setUnidades(request.unidades());
        herramienta.setDescripcion(request.descripcion());
        herramienta.setImagenUrl(request.imagenUrl());

        // No modificamos herramienta.activa al editar.

        Herramienta actualizada = herramientaRepository.save(herramienta);

        return mapearAEResponse(actualizada);
    }


    private HerramientaResponse mapearAEResponse(Herramienta h) {
        return new HerramientaResponse(
                h.getId(), h.getNombre(), h.getCategoria(), h.getDescripcion(),
                h.getPrecioDia(), h.getDeposito(), h.getUnidades(), h.getImagenUrl(), h.isActiva()
        );
    }
}