package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.SolicitudCita;
import com.tattooage.tattooage_backend.repository.SolicitudCitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
public class SolicitudCitaController {

    private final SolicitudCitaRepository solicitudCitaRepository;

    @GetMapping
    public ResponseEntity<List<SolicitudCita>> getAll() {
        return ResponseEntity.ok(solicitudCitaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudCita> getById(@PathVariable Integer id) {
        return solicitudCitaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SolicitudCita> create(@RequestBody SolicitudCita solicitud) {
        SolicitudCita saved = solicitudCitaRepository.save(solicitud);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<SolicitudCita> updateEstado(
            @PathVariable Integer id,
            @RequestParam String estado) {
        return solicitudCitaRepository.findById(id)
                .map(s -> {
                    s.setEstado(SolicitudCita.EstadoSolicitud.valueOf(estado));
                    return ResponseEntity.ok(solicitudCitaRepository.save(s));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}