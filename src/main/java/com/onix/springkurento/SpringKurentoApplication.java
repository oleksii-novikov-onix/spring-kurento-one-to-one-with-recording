package com.onix.springkurento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@SuppressWarnings({"CheckStyle", "HideUtilityClassConstructor"})
public class SpringKurentoApplication extends SpringBootServletInitializer {

    public static void main(final String[] args) {
        SpringApplication.run(SpringKurentoApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder builder) {
        return builder.sources(SpringKurentoApplication.class);
    }

}
