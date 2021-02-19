package com.onix.springkurento.config;

import org.kurento.client.KurentoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KurentoClientConfig {

    @Bean
    public KurentoClient kurentoClient() {
        return KurentoClient.create();
    }

}
