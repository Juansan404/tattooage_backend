package com.tattooage.tattooage_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes_directos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MensajeDirecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Integer idMensaje;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_conversacion", nullable = false)
    private Conversacion conversacion;

    @ManyToOne(fetch = FetchType.EAGER)
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
