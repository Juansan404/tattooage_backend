package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.dto.AuthRequest;
import com.tattooage.tattooage_backend.dto.AuthResponse;
import com.tattooage.tattooage_backend.dto.RegisterRequest;
import com.tattooage.tattooage_backend.entity.Usuario;
import com.tattooage.tattooage_backend.repository.UsuarioRepository;
import com.tattooage.tattooage_backend.security.JwtUtil;
import com.tattooage.tattooage_backend.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Auth", description = "Registro e inicio de sesión de usuarios")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    @Operation(summary = "Registrar usuario")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El email ya está registrado");
        }

        boolean esArtista = request.getRol() == Usuario.RolUsuario.ARTISTA;

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .apellidos(request.getApellidos())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .telefono(request.getTelefono())
                .rol(request.getRol() != null ? request.getRol() : Usuario.RolUsuario.CLIENTE)
                .activo(true)
                .estadoRegistro(esArtista ? Usuario.EstadoRegistro.PENDIENTE : Usuario.EstadoRegistro.ACTIVO)
                .build();

        usuarioRepository.save(usuario);

        if (esArtista) {
            emailService.sendArtistPendingAdmin(usuario.getEmail(), usuario.getNombre());
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new AuthResponse(null, usuario.getIdUsuario(), usuario.getEmail(), usuario.getRol().name(), "PENDIENTE"));
        }

        String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol().name());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(token, usuario.getIdUsuario(), usuario.getEmail(), usuario.getRol().name(), "ACTIVO"));
    }

    @Operation(summary = "Iniciar sesión")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        return usuarioRepository.findByEmail(request.getEmail())
                .filter(u -> Boolean.TRUE.equals(u.getActivo()))
                .filter(u -> passwordEncoder.matches(request.getPassword(), u.getPasswordHash()))
                .map(u -> {
                    if (u.getEstadoRegistro() == Usuario.EstadoRegistro.PENDIENTE) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .<Object>body(Map.of("estadoRegistro", "PENDIENTE", "email", u.getEmail()));
                    }
                    if (u.getEstadoRegistro() == Usuario.EstadoRegistro.RECHAZADO) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .<Object>body(Map.of("estadoRegistro", "RECHAZADO"));
                    }
                    String token = jwtUtil.generarToken(u.getEmail(), u.getRol().name());
                    return ResponseEntity.ok()
                            .<Object>body(new AuthResponse(token, u.getIdUsuario(), u.getEmail(), u.getRol().name(), "ACTIVO"));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @Operation(summary = "Consultar estado de registro (polling pantalla de espera)")
    @GetMapping("/estado")
    public ResponseEntity<?> estado(@RequestParam String email) {
        return usuarioRepository.findByEmail(email)
                .map(u -> ResponseEntity.ok(Map.of("estadoRegistro", u.getEstadoRegistro().name())))
                .orElse(ResponseEntity.notFound().build());
    }
}
