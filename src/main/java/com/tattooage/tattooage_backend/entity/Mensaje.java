package com.tattooage.tattooage_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Integer idMensaje;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false)
    private SolicitudCita solicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_remitente", nullable = false)
    private Usuario remitente;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenido;

    @Builder.Default
    @Column(nullable = false)
    private Boolean leido = false;

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;
}
