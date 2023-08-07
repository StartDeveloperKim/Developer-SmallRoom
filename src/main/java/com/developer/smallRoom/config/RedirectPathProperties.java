package com.developer.smallRoom.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "redirect")
public class RedirectPathProperties {

    private String index;
    private String logout;
    private String token;
}
