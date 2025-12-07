package com.deiz0n.studfit.infrastructure.aws;

import com.deiz0n.studfit.domain.events.ExcluirAtestadoS3Event;
import com.deiz0n.studfit.domain.events.GerarUrlAtestadoS3Event;
import com.deiz0n.studfit.domain.events.RealizarUploadAtestadoS3Event;
import com.deiz0n.studfit.domain.exceptions.aluno.AtestadoNotSavedException;
import com.deiz0n.studfit.domain.utils.GerarNomeAtestado;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.StorageClass;

import java.io.IOException;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucket;
    private final ApplicationEventPublisher eventPublisher;

    public S3Service(
            S3Client s3Client,
            @Value("${aws.s3.bucket}") String bucket,
            ApplicationEventPublisher eventPublisher
    ) {
        this.s3Client = s3Client;
        this.bucket = bucket;
        this.eventPublisher = eventPublisher;
    }

    @EventListener
    public void uploadAtestado(RealizarUploadAtestadoS3Event s3Event) {
        var nomeAtestado = GerarNomeAtestado.gerarNome(s3Event.alunoId());

        try {
            var putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(nomeAtestado)
                    .storageClass(StorageClass.GLACIER_IR)
                    .contentType(s3Event.atestado().getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(s3Event.atestado().getBytes()));

            var urlAtestado = String.format("https://%s.s3.amazonaws.com/%s", bucket, nomeAtestado);
            eventPublisher.publishEvent(new GerarUrlAtestadoS3Event(urlAtestado, s3Event.alunoId()));
        } catch (IOException e) {
            throw new AtestadoNotSavedException("Erro ao salvar atestado");
        }
    }

    @EventListener
    public void excluirAtestado(ExcluirAtestadoS3Event s3Event) {
        var deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(s3Event.nomeAtestado())
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

}
