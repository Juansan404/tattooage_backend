package com.tattooage.tattooage_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes_cita")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SolicitudCita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Integer idSolicitud;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario cliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_artista", nullable = false)
    private Usuario artista;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "zona_cuerpo", length = 50)
    private String zonaCuerpo;

    @Column(length = 20)
    private String tamano;

    @Column(name = "presupuesto_aprox", precision = 10, scale = 2)
    private BigDecimal presupuestoAprox;

    @Column(name = "fecha_preferida")
    private LocalDate fechaPreferida;

    @Column(name = "foto_referencia", columnDefinition = "TEXT")
    private String fotoReferencia;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(length = 20, columnDefinition = "VARCHAR(20)")
    private EstadoSolicitud estado = EstadoSolicitud.Pendiente;

    @Column(name = "notas_artista", columnDefinition = "TEXT")
    private String notasArtista;

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;

    public enum EstadoSolicitud {
        Pendiente, Aceptada, Rechazada, Completada
    }
}