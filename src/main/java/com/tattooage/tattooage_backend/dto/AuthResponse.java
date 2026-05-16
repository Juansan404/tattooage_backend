package com.tattooage.tattooage_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Integer idUsuario;
    private String email;
    private String rol;
    private String estadoRegistro;
}
