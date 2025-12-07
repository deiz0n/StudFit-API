package com.deiz0n.studfit.unit.services;

import com.deiz0n.studfit.domain.events.ExcluirAtestadoS3Event;
import com.deiz0n.studfit.domain.events.GerarUrlAtestadoS3Event;
import com.deiz0n.studfit.domain.events.RealizarUploadAtestadoS3Event;
import com.deiz0n.studfit.domain.utils.GerarNomeAtestado;
import com.deiz0n.studfit.infrastructure.aws.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.StorageClass;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class S3ServiceTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private S3Service s3Service;
    private final String BUCKET_NAME = "teste";
    private final UUID ALUNO_ID = UUID.randomUUID();
    private final String NOME_ATESTADO = GerarNomeAtestado.gerarNome(ALUNO_ID);

    @BeforeEach
    void setUp() {
        s3Service = new S3Service(s3Client, BUCKET_NAME, eventPublisher);
    }

    @Test
    void whenUploadAtestadoThenPublishUrlEvent() throws IOException {
        byte[] conteudoArquivo = NOME_ATESTADO.getBytes();
        var contentType = "application/pdf";

        when(multipartFile.getBytes()).thenReturn(conteudoArquivo);
        when(multipartFile.getContentType()).thenReturn(contentType);
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());

        var evento = new RealizarUploadAtestadoS3Event(multipartFile, ALUNO_ID);
        s3Service.uploadAtestado(evento);

        var putObjectCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);
        verify(s3Client, times(1)).putObject(putObjectCaptor.capture(), any(RequestBody.class));

        PutObjectRequest requestCapturado = putObjectCaptor.getValue();
        assertEquals(BUCKET_NAME, requestCapturado.bucket());
        assertEquals(StorageClass.GLACIER_IR, requestCapturado.storageClass());
        assertEquals(contentType, requestCapturado.contentType());
        assertTrue(requestCapturado.key().contains(".pdf"));

        var eventCaptor = ArgumentCaptor.forClass(GerarUrlAtestadoS3Event.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());

        GerarUrlAtestadoS3Event eventoPublicado = eventCaptor.getValue();
        assertTrue(eventoPublicado.url().startsWith("https://" + BUCKET_NAME + ".s3.amazonaws.com/"));
        assertTrue(eventoPublicado.url().endsWith(".pdf"));
        assertEquals(ALUNO_ID, eventoPublicado.alunoId());
    }

    @Test
    void whenUploadAtestadoThenThrowsIOException() throws IOException {
        when(multipartFile.getBytes()).thenThrow(new IOException("Erro de leitura"));

        var evento = new RealizarUploadAtestadoS3Event(multipartFile, ALUNO_ID);

        assertThrows(RuntimeException.class, () -> s3Service.uploadAtestado(evento));

        verifyNoInteractions(s3Client);
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void whenExcluirAtestadoThenDeleteFromS3() {
        var evento = new ExcluirAtestadoS3Event(NOME_ATESTADO);

        s3Service.excluirAtestado(evento);

        var captor = ArgumentCaptor.forClass(DeleteObjectRequest.class);

        verify(s3Client, times(1)).deleteObject(captor.capture());

        DeleteObjectRequest requestCapturado = captor.getValue();

        assertEquals(BUCKET_NAME, requestCapturado.bucket());
        assertEquals(NOME_ATESTADO, requestCapturado.key());
    }

}
