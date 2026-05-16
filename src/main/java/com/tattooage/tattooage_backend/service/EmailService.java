package com.tattooage.tattooage_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendPasswordResetCode(String toEmail, String code) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(toEmail);
        msg.setSubject("TattooAge – Código para restablecer contraseña");
        msg.setText(
            "Hola,\n\n" +
            "Tu código de verificación para restablecer la contraseña es:\n\n" +
            "   " + code + "\n\n" +
            "Este código es válido durante 15 minutos.\n" +
            "Si no has solicitado este cambio, ignora este correo.\n\n" +
            "— El equipo de TattooAge"
        );
        mailSender.send(msg);
    }

    public void sendArtistPendingAdmin(String artistEmail, String nombre) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(from);
        msg.setSubject("TattooAge – Nueva solicitud de registro como artista");
        msg.setText(
            "Hola,\n\n" +
            "El usuario " + nombre + " (" + artistEmail + ") ha solicitado registrarse como artista.\n\n" +
            "Accede a la aplicación de escritorio para aprobar o rechazar la solicitud.\n\n" +
            "— Sistema TattooAge"
        );
        mailSender.send(msg);
    }

    public void sendArtistApproved(String artistEmail, String nombre) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(artistEmail);
        msg.setSubject("TattooAge – ¡Tu cuenta de artista ha sido aprobada!");
        msg.setText(
            "Hola " + nombre + ",\n\n" +
            "¡Enhorabuena! Tu solicitud para unirte a TattooAge como artista ha sido aprobada.\n\n" +
            "Ya puedes iniciar sesión y comenzar a compartir tu trabajo.\n\n" +
            "— El equipo de TattooAge"
        );
        mailSender.send(msg);
    }

    public void sendArtistRejected(String artistEmail, String nombre) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(artistEmail);
        msg.setSubject("TattooAge – Solicitud de registro no aprobada");
        msg.setText(
            "Hola " + nombre + ",\n\n" +
            "Lamentamos informarte de que tu solicitud para unirte a TattooAge como artista no ha sido aprobada en este momento.\n\n" +
            "Si crees que es un error, puedes ponerte en contacto con nosotros.\n\n" +
            "— El equipo de TattooAge"
        );
        mailSender.send(msg);
    }

    public void sendAdminNotification(String userEmail) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(from);
        msg.setSubject("TattooAge – Solicitud de restablecimiento de contraseña");
        msg.setText(
            "El usuario " + userEmail + " ha solicitado restablecer su contraseña.\n\n" +
            "Si el usuario tiene problemas con el correo automático, " +
            "puedes asistirle manualmente desde el panel de administración.\n\n" +
            "— Sistema TattooAge"
        );
        mailSender.send(msg);
    }
}
