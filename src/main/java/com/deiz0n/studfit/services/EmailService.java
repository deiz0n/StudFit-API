package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.EmailDTO;
import com.deiz0n.studfit.domain.events.SentEmailRecoveryPasswordEvent;
import com.deiz0n.studfit.domain.exceptions.usuario.SendEmailException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String emailFrom;
    private JavaMailSender mailSender;
    private Configuration freeMakerConfig;

    public EmailService(JavaMailSender mailSender, Configuration freeMakerConfig) {
        this.mailSender = mailSender;
        this.freeMakerConfig = freeMakerConfig;
    }

    private void sendMail(EmailDTO message) {
        var content = processTemplate(message);
        var mimeMessage = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        try {
            helper.setTo(message.getDestinatario());
            helper.setSubject(message.getTitulo());
            helper.setText(content, true);
            helper.setFrom(emailFrom);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new SendEmailException("Erro ao enviar o email");
        }
    }

    private String processTemplate(EmailDTO message) {
        try {
            Template template = freeMakerConfig.getTemplate(message.getConteudo());

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, message.getVariaveis());
        } catch (Exception e) {
            throw new SendEmailException("Erro ao renderizar template do email");
        }
    }

    @EventListener
    private void sendRecoveryPassword(SentEmailRecoveryPasswordEvent sentEmailRecoveryPasswordEvent) {
        var email = EmailDTO.builder()
                .destinatario(sentEmailRecoveryPasswordEvent.getDestinatario())
                .titulo("Solicitação para alteração de senha")
                .variavel("codigo", sentEmailRecoveryPasswordEvent.getCodigo())
                .conteudo("recovery-password.html")
                .build();

        sendMail(email);
    }
}
