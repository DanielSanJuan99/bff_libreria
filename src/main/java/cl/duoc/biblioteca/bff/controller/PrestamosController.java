package cl.duoc.biblioteca.bff.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.biblioteca.bff.client.EventPublisherClient;
import cl.duoc.biblioteca.bff.client.FunctionsGatewayClient;
import cl.duoc.biblioteca.bff.dto.PrestamoDto;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamosController {

    private static final String EVENT_PRESTAMO_CREADO = "Prestamo.Creado";
    private static final String EVENT_PRESTAMO_DEVUELTO = "Prestamo.Devuelto";

    private final FunctionsGatewayClient functionsGatewayClient;
    private final EventPublisherClient eventPublisherClient;

    /**
     * Crea el controlador de préstamos.
     *
     * @param functionsGatewayClient cliente gateway de tipo {@link FunctionsGatewayClient}.
     * @param eventPublisherClient cliente publisher de eventos de tipo {@link EventPublisherClient}.
     */
    public PrestamosController(FunctionsGatewayClient functionsGatewayClient,
                               EventPublisherClient eventPublisherClient) {
        this.functionsGatewayClient = functionsGatewayClient;
        this.eventPublisherClient = eventPublisherClient;
    }

    /**
     * Lista todos los préstamos.
     *
     * @return respuesta HTTP en {@link ResponseEntity} con {@link List} de {@link PrestamoDto}.
     */
    @GetMapping
    public ResponseEntity<List<PrestamoDto>> listar() {
        return ResponseEntity.ok(functionsGatewayClient.getPrestamos());
    }

    /**
     * Obtiene un préstamo por su identificador.
     *
     * @param id identificador del préstamo en {@link String}.
     * @return respuesta HTTP en {@link ResponseEntity} con un {@link PrestamoDto}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PrestamoDto> obtener(@PathVariable String id) {
        return ResponseEntity.ok(functionsGatewayClient.getPrestamoById(id));
    }

    /**
     * Crea un nuevo préstamo y publica el evento {@code Prestamo.Creado}.
     *
     * @param request datos del préstamo en {@link PrestamoDto}.
     * @return respuesta HTTP en {@link ResponseEntity} con el {@link PrestamoDto} creado.
     */
    @PostMapping
    public ResponseEntity<PrestamoDto> crear(@Valid @RequestBody PrestamoDto request) {
        PrestamoDto creado = functionsGatewayClient.createPrestamo(request);
        eventPublisherClient.publishAsync(
                EVENT_PRESTAMO_CREADO,
                "biblioteca/prestamos/" + creado.id(),
                creado);
        return ResponseEntity.ok(creado);
    }

    /**
     * Actualiza un préstamo existente y publica {@code Prestamo.Devuelto} si el
     * nuevo estado corresponde a una devolución.
     *
     * @param id identificador del préstamo en {@link String}.
     * @param request datos actualizados en {@link PrestamoDto}.
     * @return respuesta HTTP en {@link ResponseEntity} con el {@link PrestamoDto} actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PrestamoDto> actualizar(@PathVariable String id, @Valid @RequestBody PrestamoDto request) {
        PrestamoDto actualizado = functionsGatewayClient.updatePrestamo(id, request);
        if (actualizado != null && actualizado.estado() != null
                && "DEVUELTO".equalsIgnoreCase(actualizado.estado())) {
            eventPublisherClient.publishAsync(
                    EVENT_PRESTAMO_DEVUELTO,
                    "biblioteca/prestamos/" + actualizado.id(),
                    actualizado);
        }
        return ResponseEntity.ok(actualizado);
    }

    /**
     * Elimina un préstamo por su identificador.
     *
     * @param id identificador del préstamo en {@link String}.
     * @return respuesta HTTP en {@link ResponseEntity} con resultado en {@link Map}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable String id) {
        return ResponseEntity.ok(functionsGatewayClient.deletePrestamo(id));
    }
}
