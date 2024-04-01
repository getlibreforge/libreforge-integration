package com.libreforge.integration.security.oauth2.google.config;

import com.libreforge.integration.security.oauth2.google.service.InMemoryRefreshTokenStorage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:oauth2-google.properties")
@ComponentScan("com.libreforge.integration.security.oauth2.google")
public class SecurityOAuth2GoogleConfig {

    public InMemoryRefreshTokenStorage refreshTokenStorage() {
        return new InMemoryRefreshTokenStorage();
    }
}
