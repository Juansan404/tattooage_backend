package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.Notificacion;
import com.tattooage.tattooage_backend.repository.NotificacionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Notificaciones", description = "Notificaciones de actividad del usuario")
@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionRepository notificacionRepository;

    @Operation(summary = "Listar notificaciones de un usuario (más recientes primero)")
    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<Notificacion>> getByUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(notificacionRepository.findByReceptorIdUsuarioOrderByCreadoEnDesc(idUsuario));
    }

    @Operation(summary = "Contar notificaciones no leídas de un usuario")
    @GetMapping("/{idUsuario}/no-leidas")
    public ResponseEntity<Map<String, Long>> countNoLeidas(@PathVariable Integer idUsuario) {
        long count = notificacionRepository.countByReceptorIdUsuarioAndLeidaFalse(idUsuario);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @Operation(summary = "Marcar una notificación como leída")
    @PutMapping("/{id}/leer")
    public ResponseEntity<Void> marcarLeida(@PathVariable Integer id) {
        return notificacionRepository.findById(id).map(n -> {
            n.setLeida(true);
            notificacionRepository.save(n);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Marcar todas las notificaciones de un usuario como leídas")
    @PutMapping("/leer-todas/{idUsuario}")
    @Transactional
    public ResponseEntity<Void> marcarTodasLeidas(@PathVariable Integer idUsuario) {
        notificacionRepository.marcarTodasLeidas(idUsuario);
        return ResponseEntity.ok().build();
    }
}
