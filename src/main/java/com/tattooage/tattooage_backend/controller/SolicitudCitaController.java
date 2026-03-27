package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.SolicitudCita;
import com.tattooage.tattooage_backend.repository.SolicitudCitaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Solicitudes de Cita", description = "Gestión de solicitudes de cita entre clientes y artistas. Requiere autenticación.")
@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
public class SolicitudCitaController {

    private final SolicitudCitaRepository solicitudCitaRepository;

    @Operation(summary = "Listar solicitudes", description = "Devuelve todas las solicitudes de cita.")
    @GetMapping
    public ResponseEntity<List<SolicitudCita>> getAll() {
        return ResponseEntity.ok(solicitudCitaRepository.findAll());
    }

    @Operation(summary = "Obtener solicitud por ID", description = "Devuelve una solicitud concreta. Devuelve 404 si no existe.")
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudCita> getById(@PathVariable Integer id) {
        return solicitudCitaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear solicitud", description = "Crea una nueva solicitud de cita. El estado inicial es Pendiente.")
    @PostMapping
    public ResponseEntity<SolicitudCita> create(@RequestBody SolicitudCita solicitud) {
        SolicitudCita saved = solicitudCitaRepository.save(solicitud);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Actualizar estado de solicitud")
    @PutMapping("/{id}/estado")
    public ResponseEntity<SolicitudCita> updateEstado(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {
        return solicitudCitaRepository.findById(id)
                .map(s -> {
                    s.setEstado(SolicitudCita.EstadoSolicitud.valueOf(body.get("estado")));
                    return ResponseEntity.ok(solicitudCitaRepository.save(s));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}