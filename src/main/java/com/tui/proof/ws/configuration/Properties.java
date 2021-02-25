package com.tui.proof.ws.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class Properties {

    @Value("${apiKey}")
    private String apiKey;

    @Value("${secret}")
    private String secret;

}