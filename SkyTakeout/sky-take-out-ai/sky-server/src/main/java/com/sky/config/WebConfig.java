package com.sky.config;

import com.sky.common.JwtTokenService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
@EnableConfigurationProperties(SkyProperties.class)
public class WebConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;
    private final SkyProperties properties;

    public WebConfig(AuthInterceptor authInterceptor, SkyProperties properties) {
        this.authInterceptor = authInterceptor;
        this.properties = properties;
    }

    @Bean
    JwtTokenService jwtTokenService(SkyProperties properties) {
        return new JwtTokenService(properties.getJwtSecret(), properties.getJwtTtlSeconds());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/admin/**", "/user/**")
                .excludePathPatterns(
                        "/admin/employee/login",
                        "/user/user/login",
                        "/admin/shop/status",
                        "/user/shop/status",
                        "/uploads/**",
                        "/doc.html",
                        "/webjars/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**"
                );
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(false);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Path.of(properties.getUploadDir()).toAbsolutePath();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath.toUri().toString() + "/");
    }
}
