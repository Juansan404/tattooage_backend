package com.tattooage.tattooage_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estilos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Estilo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estilo")
    private Integer idEstilo;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;
}
