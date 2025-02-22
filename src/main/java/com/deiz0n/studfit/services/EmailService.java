package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.EmailDTO;
import com.deiz0n.studfit.domain.dtos.UsuarioDTO;
import com.deiz0n.studfit.domain.events.*;
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
    private final JavaMailSender mailSender;
    private final Configuration freeMakerConfig;

    public EmailService(JavaMailSender mailSender, Configuration freeMakerConfig) {
        this.mailSender = mailSender;
        this.freeMakerConfig = freeMakerConfig;
    }

    private void enviarEmail(EmailDTO message) {
        var conteudo = processarTemplate(message);
        var mimeMessage = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        try {
            helper.setTo(message.getDestinatario());
            helper.setSubject(message.getTitulo());
            helper.setText(conteudo, true);
            helper.setFrom(emailFrom);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar o email", e);
        }
    }

    private String processarTemplate(EmailDTO message) {
        try {
            Template template = freeMakerConfig.getTemplate(message.getConteudo());

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, message.getVariaveis());
        } catch (Exception e) {
            throw new SendEmailException("Erro ao renderizar template do email");
        }
    }

    @EventListener
    private void enviarRecuperacaoSenha(EnviarEmailRecuperacaoSenhaEvent evento) {
        var email = EmailDTO.builder()
                .destinatario(evento.destinatario())
                .titulo("Solicitação para alteração de senha")
                .variavel("codigo", evento.codigo())
                .conteudo("recovery-password.html")
                .build();

        enviarEmail(email);
    }

    @EventListener
    private void enviarConfirmacaoSenhaAlterada(EnviarEmailAlteracaoSenhaEvent evento) {
        var email = EmailDTO.builder()
                .destinatario(evento.destinatario())
                .titulo("Sua senha foi alterada com sucesso")
                .conteudo("updated-password-success.html")
                .build();

        enviarEmail(email);
    }

    @EventListener
    private void enviarConfirmacaoExclucao(NotificarAlunoCadastroExcluidoEvent evento) {
        var email = EmailDTO.builder()
                .destinatario(evento.destinatario())
                .titulo("Exclusão de cadastro")
                .variavel("nome_aluno", evento.nome())
                .conteudo("removed-by-absences.html")
                .build();

        enviarEmail(email);
    }

    @EventListener
    private void enviarConfirmacaoAlunoExcluido(NotificarUsuarioCadastroExcluidoEvent evento) {
        var listOfEmails = evento.usuarios()
                .stream()
                .map(UsuarioDTO::getEmail)
                .toArray(String[]::new);

        var email = EmailDTO.builder()
                .destinatario(listOfEmails)
                .titulo("Exclusão de cadastro")
                .variavel("nome_aluno", evento.nomeAluno())
                .conteudo("aluno-removed.html")
                .build();

        enviarEmail(email);
    }

    @EventListener
    private void enviarConfirmacaoEfetivacao(NotificarAlunoCadastroEfetivado evento) {
        var email = EmailDTO.builder()
                .destinatario(evento.destinatario())
                .titulo("Efetivação de cadastro")
                .variavel("nome_aluno", evento.nomeAluno())
                .conteudo("aluno-efetivado.html")
                .build();

        enviarEmail(email);
    }

    @EventListener
    private void enviarConfirmacaoAlunoEfetivado(NotificarUsuarioCadastroEfetivadoEvent evento) {
        var email = EmailDTO.builder()
                .destinatario(evento.destinatario())
                .titulo("Efetivação de cadastro")
                .variavel("nome_aluno", evento.nomeAluno())
                .conteudo("aluno-efetivado-to-usuarios.html")
                .build();

        enviarEmail(email);
    }
}
