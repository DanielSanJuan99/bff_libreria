package cl.duoc.biblioteca.bff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    /**
     * Construye el cliente HTTP base para consumir Azure Functions.
     *
     * @param properties propiedades de configuración en {@link FunctionsProperties}.
     * @return cliente configurado de tipo {@link RestClient}.
     */
    @Bean
    RestClient functionsRestClient(FunctionsProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .build();
    }
}
