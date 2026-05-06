package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.Conversacion;
import com.tattooage.tattooage_backend.entity.MensajeDirecto;
import com.tattooage.tattooage_backend.entity.Usuario;
import com.tattooage.tattooage_backend.repository.ConversacionRepository;
import com.tattooage.tattooage_backend.repository.MensajeDirectoRepository;
import com.tattooage.tattooage_backend.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Conversaciones", description = "Mensajes directos entre usuarios")
@RestController
@RequestMapping("/api/conversaciones")
@RequiredArgsConstructor
public class ConversacionController {

    private final ConversacionRepository conversacionRepository;
    private final MensajeDirectoRepository mensajeDirectoRepository;
    private final UsuarioRepository usuarioRepository;

    @Operation(summary = "Listar todas las conversaciones (admin)")
    @GetMapping
    public ResponseEntity<List<Conversacion>> getAll() {
        return ResponseEntity.ok(conversacionRepository.findAll());
    }

    @Operation(summary = "Obtener una conversación por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Conversacion> getById(@PathVariable Integer id) {
        return conversacionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar conversaciones de un usuario")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Conversacion>> getByUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(conversacionRepository.findByUsuarioId(idUsuario));
    }

    @Operation(summary = "Obtener o crear conversación entre dos usuarios")
    @PostMapping("/find-or-create")
    public ResponseEntity<Conversacion> findOrCreate(@RequestBody Map<String, Integer> body) {
        Integer idU1 = body.get("idUsuario1");
        Integer idU2 = body.get("idUsuario2");

        return conversacionRepository.findByUsuarios(idU1, idU2)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    Usuario u1 = usuarioRepository.getReferenceById(idU1);
                    Usuario u2 = usuarioRepository.getReferenceById(idU2);
                    Conversacion nueva = Conversacion.builder()
                            .usuario1(u1).usuario2(u2).build();
                    Conversacion saved = conversacionRepository.save(nueva);
                    return ResponseEntity.ok(conversacionRepository.findById(saved.getIdConversacion()).orElse(saved));
                });
    }

    @Operation(summary = "Obtener mensajes de una conversación")
    @GetMapping("/{id}/mensajes")
    @Transactional
    public ResponseEntity<List<MensajeDirecto>> getMensajes(
            @PathVariable Integer id,
            @RequestParam(required = false) Integer idUsuario) {
        List<MensajeDirecto> mensajes =
                mensajeDirectoRepository.findByConversacionIdConversacionOrderByCreadoEnAsc(id);
        if (idUsuario != null) {
            mensajeDirectoRepository.marcarLeidosEnConversacion(id, idUsuario);
        }
        return ResponseEntity.ok(mensajes);
    }

    @Operation(summary = "Enviar mensaje directo (REST fallback)")
    @PostMapping("/{id}/mensajes")
    public ResponseEntity<MensajeDirecto> enviarMensaje(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {
        Integer idRemitente = (Integer) body.get("idRemitente");
        String contenido = (String) body.get("contenido");

        Conversacion conv = conversacionRepository.findById(id).orElseThrow();
        Usuario remitente = usuarioRepository.findById(idRemitente).orElseThrow();
        MensajeDirecto msg = MensajeDirecto.builder()
                .conversacion(conv).remitente(remitente).contenido(contenido).build();
        return ResponseEntity.ok(mensajeDirectoRepository.save(msg));
    }

    @Operation(summary = "Contar mensajes no leídos del usuario en todas sus conversaciones")
    @GetMapping("/no-leidos/{idUsuario}")
    public ResponseEntity<Map<String, Long>> countNoLeidos(@PathVariable Integer idUsuario) {
        List<Conversacion> convs = conversacionRepository.findByUsuarioId(idUsuario);
        long total = convs.stream()
                .mapToLong(c -> mensajeDirectoRepository
                        .countByConversacionIdConversacionAndRemitenteIdUsuarioNotAndLeidoFalse(
                                c.getIdConversacion(), idUsuario))
                .sum();
        return ResponseEntity.ok(Map.of("count", total));
    }
}
