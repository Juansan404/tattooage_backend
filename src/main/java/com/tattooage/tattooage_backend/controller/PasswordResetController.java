package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.PasswordResetToken;
import com.tattooage.tattooage_backend.entity.Usuario;
import com.tattooage.tattooage_backend.repository.PasswordResetTokenRepository;
import com.tattooage.tattooage_backend.repository.UsuarioRepository;
import com.tattooage.tattooage_backend.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@Tag(name = "Auth", description = "Registro e inicio de sesión de usuarios")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class PasswordResetController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Operation(summary = "Solicitar código de restablecimiento")
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email requerido"));
        }

        // Responder siempre OK para no revelar si el email existe
        usuarioRepository.findByEmail(email.trim()).ifPresent(usuario -> {
            tokenRepository.deleteByEmail(email.trim());

            String codigo = String.format("%06d", new Random().nextInt(1_000_000));
            PasswordResetToken token = PasswordResetToken.builder()
                    .email(email.trim())
                    .codigo(codigo)
                    .expiraEn(LocalDateTime.now().plusMinutes(15))
                    .usado(false)
                    .build();
            tokenRepository.save(token);

            try {
                emailService.sendPasswordResetCode(email.trim(), codigo);
            } catch (Exception ignored) {
                // Si falla el correo, notificamos al admin igualmente
            }
            try {
                emailService.sendAdminNotification(email.trim());
            } catch (Exception ignored) {
            }
        });

        return ResponseEntity.ok(Map.of("message", "Si el correo existe, recibirás un código en breve."));
    }

    @Operation(summary = "Confirmar código y cambiar contraseña")
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> body) {
        String email    = body.get("email");
        String codigo   = body.get("codigo");
        String password = body.get("password");

        if (email == null || codigo == null || password == null
                || email.isBlank() || codigo.isBlank() || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Todos los campos son obligatorios"));
        }
        if (password.length() < 6) {
            return ResponseEntity.badRequest().body(Map.of("error", "La contraseña debe tener al menos 6 caracteres"));
        }

        PasswordResetToken token = tokenRepository
                .findTopByEmailAndUsadoFalseOrderByExpiraEnDesc(email.trim())
                .orElse(null);

        if (token == null || !token.getCodigo().equals(codigo.trim())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Código inválido"));
        }
        if (token.getExpiraEn().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(Map.of("error", "El código ha expirado"));
        }

        Usuario usuario = usuarioRepository.findByEmail(email.trim()).orElse(null);
        if (usuario == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuario no encontrado"));
        }

        usuario.setPasswordHash(passwordEncoder.encode(password));
        usuarioRepository.save(usuario);

        token.setUsado(true);
        tokenRepository.save(token);

        return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente"));
    }
}
