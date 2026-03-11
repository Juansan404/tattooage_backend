package com.tattooage.tattooage_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "perfiles_artista")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PerfilArtista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil")
    private Integer idPerfil;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_estudio")
    private Estudio estudio;

    @Column(columnDefinition = "TEXT")
    private String especialidades;

    @Column(name = "anos_experiencia")
    private Integer anosExperiencia;

    @Column(name = "portfolio_url")
    private String portfolioUrl;

    @Builder.Default
    @Column(nullable = false)
    private Boolean disponible = true;

    @Column(name = "precio_hora", precision = 10, scale = 2)
    private BigDecimal precioHora;

    @Column(length = 100)
    private String instagram;

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;
}