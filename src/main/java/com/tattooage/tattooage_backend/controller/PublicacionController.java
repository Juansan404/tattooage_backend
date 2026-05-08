package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.Estilo;
import com.tattooage.tattooage_backend.entity.Like;
import com.tattooage.tattooage_backend.entity.Notificacion;
import com.tattooage.tattooage_backend.entity.Publicacion;
import com.tattooage.tattooage_backend.entity.PublicacionGuardada;
import com.tattooage.tattooage_backend.entity.Usuario;
import com.tattooage.tattooage_backend.repository.EstiloRepository;
import com.tattooage.tattooage_backend.repository.LikeRepository;
import com.tattooage.tattooage_backend.repository.NotificacionRepository;
import com.tattooage.tattooage_backend.repository.PublicacionGuardadaRepository;
import com.tattooage.tattooage_backend.repository.PublicacionRepository;
import com.tattooage.tattooage_backend.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Publicaciones", description = "Gestión de publicaciones de tatuajes")
@RestController
@RequestMapping("/api/publicaciones")
@RequiredArgsConstructor
public class PublicacionController {

    private final PublicacionRepository publicacionRepository;
    private final LikeRepository likeRepository;
    private final PublicacionGuardadaRepository guardadaRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionRepository notificacionRepository;
    private final EstiloRepository estiloRepository;

    @Operation(summary = "Listar publicaciones",
               description = "Sin parámetros devuelve todas. Con ?page=0&size=15 devuelve Page<Publicacion> paginado, ordenado por fecha desc.")
    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(defaultValue = "15")  int size) {

        if (page == null) {
            List<Publicacion> todas = publicacionRepository.findAll(
                    Sort.by("creadoEn").descending());
            return ResponseEntity.ok(todas);
        }
        Page<Publicacion> resultado = publicacionRepository
                .findAllByOrderByCreadoEnDesc(PageRequest.of(page, size));
        return ResponseEntity.ok(resultado);
    }

    @Operation(summary = "Número de publicaciones de un usuario")
    @GetMapping("/count/usuario/{idUsuario}")
    public ResponseEntity<Long> countByUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(publicacionRepository.countByUsuarioIdUsuario(idUsuario));
    }

    @Operation(summary = "Obtener publicación por ID", description = "Devuelve una publicación concreta. Devuelve 404 si no existe.")
    @GetMapping("/{id}")
    public ResponseEntity<Publicacion> getById(@PathVariable Integer id) {
        return publicacionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear publicación")
    @PostMapping
    @Transactional
    public ResponseEntity<Publicacion> create(@RequestBody Map<String, Object> body) {
        Publicacion pub = new Publicacion();

        if (body.get("usuario") instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> u = (Map<String, Object>) body.get("usuario");
            if (u.get("idUsuario") instanceof Integer id) {
                pub.setUsuario(usuarioRepository.findById(id).orElseThrow());
            }
        }
        if (body.get("fotoUrl") != null)    pub.setFotoUrl(body.get("fotoUrl").toString());
        if (body.get("descripcion") != null) pub.setDescripcion(body.get("descripcion").toString());
        if (body.get("zonaCuerpo") != null)  pub.setZonaCuerpo(body.get("zonaCuerpo").toString());
        if (body.get("estilo") != null)      pub.setEstilo(body.get("estilo").toString());

        if (body.get("estilos") instanceof List) {
            @SuppressWarnings("unchecked")
            List<String> nombres = (List<String>) body.get("estilos");
            List<Estilo> estilosEntidades = nombres.stream()
                .filter(n -> n != null && !n.isBlank())
                .map(n -> estiloRepository.findByNombreIgnoreCase(n.trim())
                    .orElseGet(() -> estiloRepository.save(
                        Estilo.builder().nombre(n.trim().toLowerCase()).build())))
                .collect(Collectors.toList());
            pub.setEstilos(estilosEntidades);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(publicacionRepository.save(pub));
    }

    @Operation(summary = "Actualizar publicación")
    @PutMapping("/{id}")
    public ResponseEntity<Publicacion> update(@PathVariable Integer id, @RequestBody Map<String, Object> datos) {
        return publicacionRepository.findById(id).map(p -> {
            if (datos.get("fotoUrl")     != null) p.setFotoUrl(datos.get("fotoUrl").toString());
            if (datos.get("descripcion") != null) p.setDescripcion(datos.get("descripcion").toString());
            if (datos.get("estilo")      != null) p.setEstilo(datos.get("estilo").toString());
            if (datos.get("zonaCuerpo")  != null) p.setZonaCuerpo(datos.get("zonaCuerpo").toString());
            return ResponseEntity.ok(publicacionRepository.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar publicación", description = "Elimina una publicación por ID. Requiere autenticación.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!publicacionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        publicacionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Toggle like", description = "Da o quita like a una publicación. Devuelve el estado actualizado.")
    @PostMapping("/{id}/like")
    @Transactional
    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable Integer id,
            @RequestBody Map<String, Integer> body) {

        Integer idUsuario = body.get("idUsuario");
        if (idUsuario == null) return ResponseEntity.badRequest().build();

        Publicacion publicacion = publicacionRepository.findById(id).orElse(null);
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (publicacion == null || usuario == null) return ResponseEntity.notFound().build();

        boolean yaLiked = likeRepository.existsByPublicacionIdPublicacionAndUsuarioIdUsuario(id, idUsuario);

        int currentCount = publicacion.getLikesCount() != null ? publicacion.getLikesCount() : 0;
        if (yaLiked) {
            likeRepository.deleteByPublicacionIdPublicacionAndUsuarioIdUsuario(id, idUsuario);
            publicacion.setLikesCount(Math.max(0, currentCount - 1));
        } else {
            likeRepository.save(Like.builder().publicacion(publicacion).usuario(usuario).build());
            publicacion.setLikesCount(currentCount + 1);
            if (publicacion.getUsuario() != null && !publicacion.getUsuario().getIdUsuario().equals(idUsuario)) {
                notificacionRepository.save(Notificacion.builder()
                        .receptor(publicacion.getUsuario())
                        .emisor(usuario)
                        .tipo(Notificacion.TipoNotificacion.LIKE)
                        .idReferencia(id)
                        .build());
            }
        }
        publicacionRepository.save(publicacion);

        long count = likeRepository.countByPublicacionIdPublicacion(id);
        return ResponseEntity.ok(Map.of("liked", !yaLiked, "likesCount", count));
    }

    @Operation(summary = "Comprobar si el usuario ha dado like")
    @GetMapping("/{id}/like/{idUsuario}")
    public ResponseEntity<Map<String, Object>> getLikeStatus(
            @PathVariable Integer id,
            @PathVariable Integer idUsuario) {

        boolean liked = likeRepository.existsByPublicacionIdPublicacionAndUsuarioIdUsuario(id, idUsuario);
        long count = likeRepository.countByPublicacionIdPublicacion(id);
        return ResponseEntity.ok(Map.of("liked", liked, "likesCount", count));
    }

    @Operation(summary = "Toggle guardar publicación")
    @PostMapping("/{id}/guardar")
    @Transactional
    public ResponseEntity<Map<String, Object>> toggleGuardar(
            @PathVariable Integer id,
            @RequestBody Map<String, Integer> body) {

        Integer idUsuario = body.get("idUsuario");
        if (idUsuario == null) return ResponseEntity.badRequest().build();

        Publicacion publicacion = publicacionRepository.findById(id).orElse(null);
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (publicacion == null || usuario == null) return ResponseEntity.notFound().build();

        boolean yaGuardado = guardadaRepository.existsByPublicacionIdPublicacionAndUsuarioIdUsuario(id, idUsuario);
        if (yaGuardado) {
            guardadaRepository.deleteByPublicacionIdPublicacionAndUsuarioIdUsuario(id, idUsuario);
        } else {
            guardadaRepository.save(PublicacionGuardada.builder().publicacion(publicacion).usuario(usuario).build());
        }
        return ResponseEntity.ok(Map.of("guardado", !yaGuardado));
    }

    @Operation(summary = "Estado de guardado de una publicación para un usuario")
    @GetMapping("/{id}/guardado/{idUsuario}")
    public ResponseEntity<Map<String, Object>> getGuardadoStatus(
            @PathVariable Integer id,
            @PathVariable Integer idUsuario) {

        boolean guardado = guardadaRepository.existsByPublicacionIdPublicacionAndUsuarioIdUsuario(id, idUsuario);
        return ResponseEntity.ok(Map.of("guardado", guardado));
    }

    @Operation(summary = "Publicaciones guardadas por un usuario")
    @GetMapping("/guardadas/{idUsuario}")
    public ResponseEntity<List<Publicacion>> getGuardadas(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(guardadaRepository.findPublicacionesByUsuarioId(idUsuario));
    }
}