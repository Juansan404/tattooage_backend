package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.Mensaje;
import com.tattooage.tattooage_backend.repository.MensajeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Mensajes", description = "Mensajes asociados a solicitudes de cita. Requiere autenticación.")
@RestController
@RequestMapping("/api/mensajes")
@RequiredArgsConstructor
public class MensajeController {

    private final MensajeRepository mensajeRepository;

    @Operation(summary = "Listar todos los mensajes")
    @GetMapping
    public ResponseEntity<List<Mensaje>> getAll() {
        return ResponseEntity.ok(mensajeRepository.findAll());
    }

    @Operation(summary = "Listar mensajes de una solicitud")
    @GetMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<List<Mensaje>> getBySolicitud(@PathVariable Integer idSolicitud) {
        return ResponseEntity.ok(mensajeRepository.findBySolicitudIdSolicitud(idSolicitud));
    }

    @Operation(summary = "Obtener mensaje por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Mensaje> getById(@PathVariable Integer id) {
        return mensajeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Enviar mensaje")
    @PostMapping
    public ResponseEntity<Mensaje> create(@RequestBody Mensaje mensaje) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mensajeRepository.save(mensaje));
    }

    @Operation(summary = "Eliminar mensaje")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!mensajeRepository.existsById(id)) return ResponseEntity.notFound().build();
        mensajeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
