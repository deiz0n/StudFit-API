package com.deiz0n.studfit.unit.services;

import com.deiz0n.studfit.domain.utils.GerarNomeAtestado;
import com.deiz0n.studfit.infrastructure.aws.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
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

    private S3Service s3Service;
    private final String BUCKET_NAME = "teste";
    private final UUID ALUNO_ID = UUID.randomUUID();
    private final String NOME_ATESTADO = GerarNomeAtestado.gerarNome(ALUNO_ID);

    @BeforeEach
    void setUp() {
        s3Service = new S3Service(s3Client, BUCKET_NAME);
    }

    @Test
    void whenUploadAtestadoThenReturnURLS3() throws IOException {
        byte[] conteudoArquivo = NOME_ATESTADO.getBytes();
        var contentType = "application/pdf";

        when(multipartFile.getBytes()).thenReturn(conteudoArquivo);
        when(multipartFile.getContentType()).thenReturn(contentType);
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());

        String urlRetornada = s3Service.uploadAtestado(multipartFile, ALUNO_ID);

        assertEquals("https://" + BUCKET_NAME + ".s3.amazonaws.com/", urlRetornada);

        var captor = ArgumentCaptor.forClass(PutObjectRequest.class);

        verify(s3Client, times(1)).putObject(captor.capture(), any(RequestBody.class));

        PutObjectRequest requestCapturado = captor.getValue();

        assertEquals(BUCKET_NAME, requestCapturado.bucket());
        assertEquals(StorageClass.GLACIER_IR, requestCapturado.storageClass());
        assertEquals(contentType, requestCapturado.contentType());

        assertTrue(requestCapturado.key().contains(".pdf"));
    }

    @Test
    void whenUploadAtestadoThenThrowsIOException() throws IOException {
        when(multipartFile.getBytes()).thenThrow(new IOException("Erro de leitura"));

        assertThrows(IOException.class, () -> {
            s3Service.uploadAtestado(multipartFile, ALUNO_ID);
        });

        verifyNoInteractions(s3Client);
    }

}
