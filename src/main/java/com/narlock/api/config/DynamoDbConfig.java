package com.narlock.api.config;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDbConfig {

  @Value("${aws.dynamodb.endpoint}")
  private String dynamoDbEndpoint;

  @Value("${aws.dynamodb.region}")
  private String region;

  @Value("${aws.dynamodb.accessKeyId}")
  private String accessKeyId;

  @Value("${aws.dynamodb.secretAccessKey}")
  private String secretAccessKey;

  @Bean
  public DynamoDbClient dynamoDbClient() {
    AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);

    return DynamoDbClient.builder()
        .endpointOverride(URI.create(dynamoDbEndpoint))
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
        .build();
  }
}
