package com.tattooage.tattooage_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "seguidores", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_seguidor", "id_seguido"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Seguidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_seguidor_rel")
    private Integer idSeguidorRel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_seguidor", nullable = false)
    private Usuario seguidor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_seguido", nullable = false)
    private Usuario seguido;

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;
}
