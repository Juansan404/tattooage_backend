package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.Notificacion;
import com.tattooage.tattooage_backend.entity.Seguidor;
import com.tattooage.tattooage_backend.entity.Usuario;
import com.tattooage.tattooage_backend.repository.NotificacionRepository;
import com.tattooage.tattooage_backend.repository.SeguidorRepository;
import com.tattooage.tattooage_backend.repository.UsuarioRepository;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Seguidores", description = "Sistema de seguimiento entre usuarios")
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class SeguidorController {

    private final SeguidorRepository seguidorRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionRepository notificacionRepository;

    @Operation(summary = "Toggle seguir/dejar de seguir a un usuario")
    @PostMapping("/{idSeguido}/seguir")
    @Transactional
    public ResponseEntity<Map<String, Object>> toggleSeguir(
            @PathVariable Integer idSeguido,
            @RequestBody Map<String, Integer> body) {

        Integer idSeguidor = body.get("idUsuario");
        if (idSeguidor == null || idSeguidor.equals(idSeguido)) return ResponseEntity.badRequest().build();

        Usuario seguidor = usuarioRepository.findById(idSeguidor).orElse(null);
        Usuario seguido  = usuarioRepository.findById(idSeguido).orElse(null);
        if (seguidor == null || seguido == null) return ResponseEntity.notFound().build();

        boolean yaSigue = seguidorRepository.existsBySeguidorIdUsuarioAndSeguidoIdUsuario(idSeguidor, idSeguido);

        if (yaSigue) {
            seguidorRepository.deleteBySeguidorIdUsuarioAndSeguidoIdUsuario(idSeguidor, idSeguido);
        } else {
            seguidorRepository.save(Seguidor.builder().seguidor(seguidor).seguido(seguido).build());
            notificacionRepository.save(Notificacion.builder()
                    .receptor(seguido)
                    .emisor(seguidor)
                    .tipo(Notificacion.TipoNotificacion.SEGUIDOR)
                    .build());
        }

        long seguidores = seguidorRepository.countBySeguidoIdUsuario(idSeguido);
        return ResponseEntity.ok(Map.of("siguiendo", !yaSigue, "seguidores", seguidores));
    }

    @Operation(summary = "Comprobar si un usuario sigue a otro")
    @GetMapping("/{idSeguido}/seguir/{idSeguidor}")
    public ResponseEntity<Map<String, Object>> getEstadoSeguir(
            @PathVariable Integer idSeguido,
            @PathVariable Integer idSeguidor) {

        boolean siguiendo = seguidorRepository.existsBySeguidorIdUsuarioAndSeguidoIdUsuario(idSeguidor, idSeguido);
        long seguidores   = seguidorRepository.countBySeguidoIdUsuario(idSeguido);
        long seguidos     = seguidorRepository.countBySeguidorIdUsuario(idSeguido);
        return ResponseEntity.ok(Map.of("siguiendo", siguiendo, "seguidores", seguidores, "seguidos", seguidos));
    }

    @Operation(summary = "Obtener contadores de un usuario")
    @GetMapping("/{id}/contadores")
    public ResponseEntity<Map<String, Long>> getContadores(@PathVariable Integer id) {
        long seguidores = seguidorRepository.countBySeguidoIdUsuario(id);
        long seguidos   = seguidorRepository.countBySeguidorIdUsuario(id);
        return ResponseEntity.ok(Map.of("seguidores", seguidores, "seguidos", seguidos));
    }

    @Operation(summary = "Lista de usuarios que siguen a este usuario")
    @GetMapping("/{id}/seguidores")
    public ResponseEntity<List<Usuario>> getSeguidores(@PathVariable Integer id) {
        return ResponseEntity.ok(seguidorRepository.findSeguidoresBySeguidoId(id));
    }

    @Operation(summary = "Lista de usuarios a los que sigue este usuario")
    @GetMapping("/{id}/seguidos")
    public ResponseEntity<List<Usuario>> getSeguidos(@PathVariable Integer id) {
        return ResponseEntity.ok(seguidorRepository.findSeguidosBySeguidorId(id));
    }
}
