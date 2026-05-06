package com.tattooage.tattooage_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Integer idNotificacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_receptor", nullable = false)
    private Usuario receptor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_emisor", nullable = false)
    private Usuario emisor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoNotificacion tipo;

    @Column(name = "id_referencia")
    private Integer idReferencia;

    @Builder.Default
    @Column(nullable = false)
    private boolean leida = false;

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    public enum TipoNotificacion {
        LIKE, COMENTARIO, SEGUIDOR, SOLICITUD
    }
}
