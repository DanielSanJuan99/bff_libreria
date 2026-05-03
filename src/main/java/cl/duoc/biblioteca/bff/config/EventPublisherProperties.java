package cl.duoc.biblioteca.bff.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "event-publisher")
public record EventPublisherProperties(String url) {
}
