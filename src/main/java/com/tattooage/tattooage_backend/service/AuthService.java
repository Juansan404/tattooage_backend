package com.tattooage.tattooage_backend.service;

import com.tattooage.tattooage_backend.dto.AuthRequest;
import com.tattooage.tattooage_backend.dto.AuthResponse;
import com.tattooage.tattooage_backend.dto.RegisterRequest;
import com.tattooage.tattooage_backend.entity.Usuario;
import com.tattooage.tattooage_backend.repository.UsuarioRepository;
import com.tattooage.tattooage_backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .apellidos(request.getApellidos())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .telefono(request.getTelefono())
                .rol(request.getRol() != null ? request.getRol() : Usuario.RolUsuario.CLIENTE)
                .activo(true)
                .build();

        usuarioRepository.save(usuario);

        String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol().name());
        return new AuthResponse(token, usuario.getIdUsuario(), usuario.getEmail(), usuario.getRol().name(), usuario.getEstadoRegistro().name());
    }

    public AuthResponse login(AuthRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .filter(u -> Boolean.TRUE.equals(u.getActivo()))
                .filter(u -> passwordEncoder.matches(request.getPassword(), u.getPasswordHash()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas"));

        String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol().name());
        return new AuthResponse(token, usuario.getIdUsuario(), usuario.getEmail(), usuario.getRol().name(), usuario.getEstadoRegistro().name());
    }
}