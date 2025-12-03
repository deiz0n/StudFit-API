package com.deiz0n.studfit.infrastructure.aws;

import com.deiz0n.studfit.domain.utils.GerarNomeAtestado;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.StorageClass;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucket;

    public S3Service(S3Client s3Client, @Value("${aws.s3.bucket}") String bucket) {
        this.s3Client = s3Client;
        this.bucket = bucket;
    }

    public String uploadAtestado(MultipartFile atestado, UUID alunoId) throws IOException {
        var nomeAtestado = GerarNomeAtestado.gerarNome(alunoId);

        var putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(nomeAtestado)
                .storageClass(StorageClass.GLACIER_IR)
                .contentType(atestado.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(atestado.getBytes()));

        return String.format("https://%s.s3.amazonaws.com/", bucket);
    }

}
