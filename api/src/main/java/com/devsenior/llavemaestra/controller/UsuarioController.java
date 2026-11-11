package com.devsenior.llavemaestra.controller;

import com.devsenior.llavemaestra.dto.EstadoRequest;
import com.devsenior.llavemaestra.dto.UsuarioRequest;
import com.devsenior.llavemaestra.dto.UsuarioResponse;
import com.devsenior.llavemaestra.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasAuthority('USUARIOS_GESTIONAR')")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CLIENTES_GESTIONAR')")
    public ResponseEntity<List<UsuarioResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listarClientes());
    }

    //@PostMapping
    //public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioRequest request) {
     //   return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(request));
   // }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAuthority('CLIENTES_GESTIONAR')")
    public ResponseEntity<UsuarioResponse> cambiarEstado(@PathVariable Long id, @RequestBody EstadoRequest body) {
        String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UsuarioResponse response = usuarioService.cambiarEstado(id, body.activo(), adminEmail);
        return ResponseEntity.ok(response);

        }
}
