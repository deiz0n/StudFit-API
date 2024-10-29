package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.EmailDTO;
import com.deiz0n.studfit.domain.events.UsuarioRecoveryPassswordEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String emailFrom;
    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private void sendMail(EmailDTO message) {
        var mimeMessage = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        try {
            helper.setTo(message.getDestinatario());
            helper.setSubject(message.getTitulo());
            helper.setText(message.getConteudo(), true);
            helper.setFrom(emailFrom);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar o enail", e);
        }
    }

    @EventListener
    private void sendRecoveryPassword(UsuarioRecoveryPassswordEvent recoveryPassswordEvent) {
        var email = EmailDTO.builder()
                .destinatario(recoveryPassswordEvent.getEmail())
                .titulo("Solicitação para alteração de senha")
                .conteudo("Seu código é: {{CÓDIGO}}")
                .build();

        sendMail(email);
    }
}
