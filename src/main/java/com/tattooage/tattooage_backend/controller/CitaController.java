package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.Cita;
import com.tattooage.tattooage_backend.repository.CitaRepository;
import com.tattooage.tattooage_backend.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Citas", description = "Gestión de citas de tatuaje. Requiere autenticación.")
@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaRepository citaRepository;
    private final UsuarioRepository usuarioRepository;

    @Operation(summary = "Listar citas", description = "Devuelve todas las citas registradas.")
    @GetMapping
    public ResponseEntity<List<Cita>> getAll() {
        return ResponseEntity.ok(citaRepository.findAll());
    }

    @Operation(summary = "Listar citas de un cliente")
    @GetMapping("/cliente/{idUsuario}")
    public ResponseEntity<List<Cita>> getByCliente(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(citaRepository.findByClienteIdUsuario(idUsuario));
    }

    @Operation(summary = "Listar citas de un artista")
    @GetMapping("/artista/{idUsuario}")
    public ResponseEntity<List<Cita>> getByArtista(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(citaRepository.findByArtistaIdUsuario(idUsuario));
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
        if (cita.getCliente() != null && cita.getCliente().getIdUsuario() != null) {
            cita.setCliente(usuarioRepository.getReferenceById(cita.getCliente().getIdUsuario()));
        }
        if (cita.getArtista() != null && cita.getArtista().getIdUsuario() != null) {
            cita.setArtista(usuarioRepository.getReferenceById(cita.getArtista().getIdUsuario()));
        }
        Cita saved = citaRepository.save(cita);
        Cita response = citaRepository.findById(saved.getIdCita()).orElse(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Actualizar cita")
    @PutMapping("/{id}")
    public ResponseEntity<Cita> update(@PathVariable Integer id, @RequestBody Cita datos) {
        return citaRepository.findById(id).map(c -> {
            if (datos.getCliente()             != null) c.setCliente(datos.getCliente());
            if (datos.getArtista()             != null) c.setArtista(datos.getArtista());
            if (datos.getFechaCita()           != null) c.setFechaCita(datos.getFechaCita());
            if (datos.getHoraInicio()          != null) c.setHoraInicio(datos.getHoraInicio());
            if (datos.getDuracionAproximada()  != null) c.setDuracionAproximada(datos.getDuracionAproximada());
            if (datos.getPrecio()              != null) c.setPrecio(datos.getPrecio());
            if (datos.getEstado()              != null) c.setEstado(datos.getEstado());
            if (datos.getSala()                != null) c.setSala(datos.getSala());
            if (datos.getNotas()               != null) c.setNotas(datos.getNotas());
            return ResponseEntity.ok(citaRepository.save(c));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cambiar estado de cita")
    @PutMapping("/{id}/estado")
    public ResponseEntity<Cita> updateEstado(@PathVariable Integer id,
                                              @RequestBody Map<String, String> body) {
        return citaRepository.findById(id).map(c -> {
            c.setEstado(Cita.EstadoCita.valueOf(body.get("estado")));
            return ResponseEntity.ok(citaRepository.save(c));
        }).orElse(ResponseEntity.notFound().build());
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