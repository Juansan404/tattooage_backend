package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.Notificacion;
import com.tattooage.tattooage_backend.entity.SolicitudCita;
import com.tattooage.tattooage_backend.repository.NotificacionRepository;
import com.tattooage.tattooage_backend.repository.SolicitudCitaRepository;
import com.tattooage.tattooage_backend.repository.UsuarioRepository;
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
    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;

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
        try {
            Integer idArtista = saved.getArtista().getIdUsuario();
            Integer idCliente = saved.getCliente().getIdUsuario();
            if (idArtista != null && idCliente != null && !idArtista.equals(idCliente)) {
                notificacionRepository.save(Notificacion.builder()
                        .receptor(usuarioRepository.getReferenceById(idArtista))
                        .emisor(usuarioRepository.getReferenceById(idCliente))
                        .tipo(Notificacion.TipoNotificacion.SOLICITUD)
                        .idReferencia(saved.getIdSolicitud())
                        .build());
            }
        } catch (Exception ignored) {}
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Actualizar solicitud completa")
    @PutMapping("/{id}")
    public ResponseEntity<SolicitudCita> update(@PathVariable Integer id,
                                                 @RequestBody SolicitudCita datos) {
        return solicitudCitaRepository.findById(id).map(s -> {
            if (datos.getCliente()          != null) s.setCliente(datos.getCliente());
            if (datos.getArtista()          != null) s.setArtista(datos.getArtista());
            if (datos.getDescripcion()      != null) s.setDescripcion(datos.getDescripcion());
            if (datos.getZonaCuerpo()       != null) s.setZonaCuerpo(datos.getZonaCuerpo());
            if (datos.getTamano()           != null) s.setTamano(datos.getTamano());
            if (datos.getPresupuestoAprox() != null) s.setPresupuestoAprox(datos.getPresupuestoAprox());
            if (datos.getFechaPreferida()   != null) s.setFechaPreferida(datos.getFechaPreferida());
            if (datos.getFotoReferencia()   != null) s.setFotoReferencia(datos.getFotoReferencia());
            if (datos.getEstado()           != null) s.setEstado(datos.getEstado());
            if (datos.getNotasArtista()     != null) s.setNotasArtista(datos.getNotasArtista());
            return ResponseEntity.ok(solicitudCitaRepository.save(s));
        }).orElse(ResponseEntity.notFound().build());
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

    @Operation(summary = "Eliminar solicitud")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!solicitudCitaRepository.existsById(id)) return ResponseEntity.notFound().build();
        solicitudCitaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}