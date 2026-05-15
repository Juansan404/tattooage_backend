package com.tattooage.tattooage_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "empresas")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Integer idEmpresa;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 100)
    private String ciudad;

    @Column(columnDefinition = "TEXT")
    private String localizacion;

    @Column(length = 100)
    private String email;

    @Column(length = 15)
    private String telefono;

    @Column(length = 200)
    private String web;

    @Column(columnDefinition = "TEXT")
    private String logo;

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;
}
