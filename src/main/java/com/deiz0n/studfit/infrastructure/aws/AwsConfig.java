package com.deiz0n.studfit.infrastructure.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    @Bean
    public S3Service s3Service(
            @Value("${aws.accessKey}") String accessKey,
            @Value("${aws.secretKey}") String secretKey,
            @Value("${aws.sessionToken:}") String sessionToken,
            @Value("${aws.region}") String region,
            @Value("${aws.s3.bucket}") String bucket
    ) {
        return new S3Service(accessKey, secretKey, sessionToken,region, bucket);
    }
}
