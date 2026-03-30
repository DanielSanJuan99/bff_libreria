package cl.duoc.biblioteca.bff.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "functions")
public record FunctionsProperties(String baseUrl) {
}
