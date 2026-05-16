package com.tattooage.tattooage_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "disenos_creados")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DisenoCreado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_diseno")
    private Integer idDiseno;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "foto_url", nullable = false, columnDefinition = "TEXT")
    private String fotoUrl;

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;
}
