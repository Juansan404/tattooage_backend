package com.tattooage.tattooage_backend.controller;

import com.tattooage.tattooage_backend.entity.Mensaje;
import com.tattooage.tattooage_backend.repository.MensajeRepository;
import com.tattooage.tattooage_backend.repository.SolicitudCitaRepository;
import com.tattooage.tattooage_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final MensajeRepository mensajeRepository;
    private final SolicitudCitaRepository solicitudCitaRepository;
    private final UsuarioRepository usuarioRepository;

    @MessageMapping("/solicitud/{solicitudId}/mensaje")
    @SendTo("/topic/solicitud/{solicitudId}")
    public Mensaje enviarMensaje(
            @DestinationVariable Integer solicitudId,
            @org.springframework.messaging.handler.annotation.Payload Map<String, Object> body) {

        Integer idRemitente = (Integer) body.get("idRemitente");
        String contenido = (String) body.get("contenido");

        Mensaje mensaje = new Mensaje();
        mensaje.setSolicitud(solicitudCitaRepository.getReferenceById(solicitudId));
        mensaje.setRemitente(usuarioRepository.findById(idRemitente).orElseThrow());
        mensaje.setContenido(contenido);

        Mensaje saved = mensajeRepository.save(mensaje);
        return mensajeRepository.findById(saved.getIdMensaje()).orElse(saved);
    }
}
