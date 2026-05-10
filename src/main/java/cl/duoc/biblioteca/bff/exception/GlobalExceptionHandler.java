package cl.duoc.biblioteca.bff.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handler global de excepciones del BFF.
 *
 * <p>Spring's {@code RestClient.retrieve().body()} lanza
 * {@link RestClientResponseException} ante cualquier respuesta non-2xx de las
 * Azure Functions aguas abajo. Sin este handler, esa excepcion se propaga sin
 * capturar y Spring devuelve un 500 generico al cliente, ocultando el status
 * real (404, 409, etc.) y el mensaje de error que la function envio.</p>
 *
 * <p>Esta clase intercepta esas excepciones y propaga el status correcto y el
 * body JSON original, manteniendo la transparencia BFF -> Function.</p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Captura cualquier respuesta de error (4xx / 5xx) que las functions
     * aguas abajo devuelvan al RestClient.
     *
     * @param ex excepcion del RestClient con status y body originales
     * @return respuesta HTTP con el mismo status (4xx) o 502 si fue 5xx,
     *         conservando el body de error de la function.
     */
    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<Map<String, Object>> handleUpstreamError(RestClientResponseException ex) {
        HttpStatusCode status = ex.getStatusCode();
        Map<String, Object> body = parseBody(ex);

        if (status.is5xxServerError()) {
            // El error vino de aguas abajo: para el cliente del BFF eso
            // representa un fallo del gateway, asi que respondemos 502.
            LOG.error("Function respondio {} (5xx): {}", status, ex.getResponseBodyAsString());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(body);
        }

        // 4xx: propagamos exactamente el mismo status (404, 409, 400...)
        LOG.warn("Function respondio {}: {}", status, ex.getResponseBodyAsString());
        return ResponseEntity.status(status).body(body);
    }

    /**
     * La function no es alcanzable (timeout, DNS, conexion rechazada).
     * Para el cliente del BFF eso es un servicio aguas abajo no disponible.
     *
     * @param ex excepcion de acceso de red
     * @return respuesta HTTP 503 con detalle del problema
     */
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Map<String, Object>> handleNetworkError(ResourceAccessException ex) {
        LOG.error("Function inalcanzable: {}", ex.getMessage());
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Servicio aguas abajo no disponible");
        body.put("detalle", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
    }

    /**
     * Parsea el body JSON original que la function envio. Si el body no es
     * JSON valido (o esta vacio), construye un map minimo con el statusText.
     */
    private Map<String, Object> parseBody(RestClientResponseException ex) {
        String body = ex.getResponseBodyAsString();
        if (body == null || body.isBlank()) {
            Map<String, Object> fallback = new LinkedHashMap<>();
            fallback.put("error", ex.getStatusText());
            return fallback;
        }
        try {
            return OBJECT_MAPPER.readValue(body, new TypeReference<>() {
            });
        } catch (Exception parseEx) {
            Map<String, Object> raw = new LinkedHashMap<>();
            raw.put("error", body);
            return raw;
        }
    }
}
