package cl.duoc.biblioteca.bff.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.biblioteca.bff.client.FunctionsGatewayClient;
import cl.duoc.biblioteca.bff.dto.NotificacionDto;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionesController {

    private final FunctionsGatewayClient functionsGatewayClient;

    /**
     * Crea el controlador de notificaciones.
     *
     * @param functionsGatewayClient cliente gateway de tipo {@link FunctionsGatewayClient}.
     */
    public NotificacionesController(FunctionsGatewayClient functionsGatewayClient) {
        this.functionsGatewayClient = functionsGatewayClient;
    }

    /**
     * Lista todas las notificaciones generadas por los consumers de Event Grid.
     *
     * @return respuesta HTTP en {@link ResponseEntity} con {@link List} de {@link NotificacionDto}.
     */
    @GetMapping
    public ResponseEntity<List<NotificacionDto>> listar() {
        return ResponseEntity.ok(functionsGatewayClient.getNotificaciones());
    }

    /**
     * Lista las notificaciones de un usuario específico.
     *
     * @param idUsuario identificador del usuario en {@link String}.
     * @return respuesta HTTP en {@link ResponseEntity} con {@link List} de {@link NotificacionDto}.
     */
    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<NotificacionDto>> listarPorUsuario(@PathVariable String idUsuario) {
        return ResponseEntity.ok(functionsGatewayClient.getNotificacionesByUsuario(idUsuario));
    }
}
