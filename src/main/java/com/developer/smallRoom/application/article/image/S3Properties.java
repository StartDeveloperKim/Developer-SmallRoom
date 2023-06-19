package com.developer.smallRoom.application.article.image;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "s3")
public class S3Properties {

    private String bucket;
    private String accessKey;
    private String secretKey;
    private String region;
}
