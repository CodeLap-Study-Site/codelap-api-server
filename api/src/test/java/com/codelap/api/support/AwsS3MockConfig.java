package com.codelap.api.support;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.codelap.integration.s3.S3Properties;
import io.findify.s3mock.S3Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AwsS3MockConfig {

    @Autowired
    S3Properties s3Properties;

    @Bean
    public S3Mock s3Mock() {
        return new S3Mock.Builder().withPort(8001).withInMemoryBackend().build();
    }

    @Bean
    public AmazonS3 amazonS3(S3Mock s3Mock){
        s3Mock.start();
        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration("http://localhost:8001", s3Properties.getRegion());
        AmazonS3 client = AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
                .build();
        client.createBucket(s3Properties.getBucket());

        return client;
    }
}
