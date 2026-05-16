package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.Usuario;
import com.tattooage.tattooage_backend.repository.UsuarioRepository;
import com.tattooage.tattooage_backend.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Admin", description = "Gestión de solicitudes de artistas. Solo ADMIN.")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;

    @Operation(summary = "Listar artistas pendientes de aprobación")
    @GetMapping("/artistas/pendientes")
    public ResponseEntity<List<Usuario>> getPendientes() {
        return ResponseEntity.ok(usuarioRepository.findByEstadoRegistro(Usuario.EstadoRegistro.PENDIENTE));
    }

    @Operation(summary = "Aprobar registro de artista")
    @PutMapping("/artistas/{id}/aprobar")
    public ResponseEntity<?> aprobar(@PathVariable Integer id) {
        return usuarioRepository.findById(id).map(u -> {
            u.setEstadoRegistro(Usuario.EstadoRegistro.ACTIVO);
            usuarioRepository.save(u);
            emailService.sendArtistApproved(u.getEmail(), u.getNombre());
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Rechazar registro de artista")
    @PutMapping("/artistas/{id}/rechazar")
    public ResponseEntity<?> rechazar(@PathVariable Integer id) {
        return usuarioRepository.findById(id).map(u -> {
            u.setEstadoRegistro(Usuario.EstadoRegistro.RECHAZADO);
            usuarioRepository.save(u);
            emailService.sendArtistRejected(u.getEmail(), u.getNombre());
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
