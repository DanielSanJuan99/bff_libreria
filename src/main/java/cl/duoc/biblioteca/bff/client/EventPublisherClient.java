package cl.duoc.biblioteca.bff.client;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.duoc.biblioteca.bff.config.EventPublisherProperties;

@Component
public class EventPublisherClient {

    private static final Logger LOG = LoggerFactory.getLogger(EventPublisherClient.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final EventPublisherProperties properties;
    private final RestClient restClient;

    public EventPublisherClient(EventPublisherProperties properties) {
        this.properties = properties;
        this.restClient = (properties != null && properties.url() != null && !properties.url().isBlank())
                ? RestClient.builder().build()
                : null;
    }

    /**
     * Publica un evento de dominio al Event Grid Topic vía la function publisher.
     * Operación fire-and-forget: si la publicación falla, se registra warning sin
     * propagar la excepción al flujo CRUD del controlador.
     *
     * @param eventType tipo de evento (ej. "Prestamo.Creado")
     * @param subject sujeto del evento (ej. "biblioteca/prestamos/123")
     * @param data payload del evento
     */
    public void publishAsync(String eventType, String subject, Object data) {
        if (restClient == null) {
            LOG.warn("EventPublisher deshabilitado: no se configuró 'event-publisher.url'");
            return;
        }

        String url = properties.url();
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("eventType", eventType);
        body.put("subject", subject);
        body.put("data", data);

        String payload;
        try {
            payload = OBJECT_MAPPER.writeValueAsString(body);
        } catch (Exception ex) {
            LOG.warn("EventPublisher: error serializando payload eventType={}: {}", eventType, ex.getMessage());
            return;
        }

        CompletableFuture.runAsync(() -> {
            try {
                String response = restClient.post()
                        .uri(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(payload)
                        .retrieve()
                        .body(String.class);
                LOG.info("EventPublisher OK eventType={} subject={} response={}", eventType, subject, response);
            } catch (Exception ex) {
                LOG.warn("EventPublisher fallo eventType={} subject={}: {}", eventType, subject, ex.getMessage());
            }
        });
    }
}
