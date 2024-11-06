package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.EmailDTO;
import com.deiz0n.studfit.domain.dtos.UsuarioDTO;
import com.deiz0n.studfit.domain.events.SentAlunoDeletedToUsuariosEvent;
import com.deiz0n.studfit.domain.events.SentEmailDeletedAlunoEfetivadoEvent;
import com.deiz0n.studfit.domain.events.SentEmailRecoveryPasswordEvent;
import com.deiz0n.studfit.domain.events.SentEmailUpdatedPasswordEvent;
import com.deiz0n.studfit.domain.exceptions.usuario.SendEmailException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.Arrays;

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
            throw new RuntimeException("Erro ao enviar o email", e);
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

    @EventListener
    private void sendConfirmUpdatedPassword(SentEmailUpdatedPasswordEvent updatedPassword) {
        var email = EmailDTO.builder()
                .destinatario(updatedPassword.getDestinatario())
                .titulo("Sua senha foi alterada com sucesso")
                .conteudo("updated-password-success.html")
                .build();

        sendMail(email);
    }

    @EventListener
    private void sendDeletedAlunoEfetivado(SentEmailDeletedAlunoEfetivadoEvent deletedAlunoEfetivado) {
        var email = EmailDTO.builder()
                .destinatario(deletedAlunoEfetivado.getDestinatario())
                .titulo("Exclusão de cadastro")
                .variavel("nome_aluno", deletedAlunoEfetivado.getNome())
                .conteudo("removed-by-absences.html")
                .build();

        sendMail(email);
    }

    @EventListener
    private void sendAlunoDeletedToUsuarios(SentAlunoDeletedToUsuariosEvent deletedToUsuarios) {
        var listOfEmails = deletedToUsuarios.getUsuarios()
                .stream()
                .map(UsuarioDTO::getEmail)
                .toArray(String[]::new);

        var email = EmailDTO.builder()
                .destinatario(listOfEmails)
                .titulo("Exclusão de cadastro")
                .variavel("nome_aluno", deletedToUsuarios.getNomeAluno())
                .conteudo("aluno-removed.html")
                .build();

        sendMail(email);
    }
}
