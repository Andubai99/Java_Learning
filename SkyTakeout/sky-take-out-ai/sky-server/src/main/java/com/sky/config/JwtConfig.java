package com.sky.config;

import com.sky.common.JwtTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Bean
    JwtTokenService jwtTokenService(SkyProperties properties) {
        return new JwtTokenService(properties.getJwtSecret(), properties.getJwtTtlSeconds());
    }
}
