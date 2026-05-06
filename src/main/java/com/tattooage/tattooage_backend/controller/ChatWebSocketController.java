package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.Mensaje;
import com.tattooage.tattooage_backend.entity.MensajeDirecto;
import com.tattooage.tattooage_backend.repository.ConversacionRepository;
import com.tattooage.tattooage_backend.repository.MensajeDirectoRepository;
import com.tattooage.tattooage_backend.repository.MensajeRepository;
import com.tattooage.tattooage_backend.repository.SolicitudCitaRepository;
import com.tattooage.tattooage_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final MensajeRepository mensajeRepository;
    private final SolicitudCitaRepository solicitudCitaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ConversacionRepository conversacionRepository;
    private final MensajeDirectoRepository mensajeDirectoRepository;

    @MessageMapping("/solicitud/{solicitudId}/mensaje")
    @SendTo("/topic/solicitud/{solicitudId}")
    public Mensaje enviarMensajeSolicitud(
            @DestinationVariable Integer solicitudId,
            @Payload Map<String, Object> body) {

        Integer idRemitente = (Integer) body.get("idRemitente");
        String contenido = (String) body.get("contenido");

        Mensaje mensaje = new Mensaje();
        mensaje.setSolicitud(solicitudCitaRepository.getReferenceById(solicitudId));
        mensaje.setRemitente(usuarioRepository.findById(idRemitente).orElseThrow());
        mensaje.setContenido(contenido);

        Mensaje saved = mensajeRepository.save(mensaje);
        return mensajeRepository.findById(saved.getIdMensaje()).orElse(saved);
    }

    @MessageMapping("/conversacion/{convId}/mensaje")
    @SendTo("/topic/conversacion/{convId}")
    public MensajeDirecto enviarMensajeDirecto(
            @DestinationVariable Integer convId,
            @Payload Map<String, Object> body) {

        Integer idRemitente = (Integer) body.get("idRemitente");
        String contenido = (String) body.get("contenido");

        MensajeDirecto msg = MensajeDirecto.builder()
                .conversacion(conversacionRepository.getReferenceById(convId))
                .remitente(usuarioRepository.findById(idRemitente).orElseThrow())
                .contenido(contenido)
                .build();

        MensajeDirecto saved = mensajeDirectoRepository.save(msg);
        return mensajeDirectoRepository.findById(saved.getIdMensaje()).orElse(saved);
    }
}
