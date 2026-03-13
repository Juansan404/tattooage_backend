package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.Cita;
import com.tattooage.tattooage_backend.repository.CitaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Citas", description = "Gestión de citas de tatuaje. Requiere autenticación.")
@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaRepository citaRepository;

    @Operation(summary = "Listar citas", description = "Devuelve todas las citas registradas.")
    @GetMapping
    public ResponseEntity<List<Cita>> getAll() {
        return ResponseEntity.ok(citaRepository.findAll());
    }

    @Operation(summary = "Obtener cita por ID", description = "Devuelve una cita concreta. Devuelve 404 si no existe.")
    @GetMapping("/{id}")
    public ResponseEntity<Cita> getById(@PathVariable Integer id) {
        return citaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear cita", description = "Crea una nueva cita de tatuaje. Devuelve 201 con la cita creada.")
    @PostMapping
    public ResponseEntity<Cita> create(@RequestBody Cita cita) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(citaRepository.save(cita));
    }

    @Operation(summary = "Eliminar cita", description = "Elimina una cita por ID. Devuelve 204 si se elimina correctamente.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!citaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        citaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}