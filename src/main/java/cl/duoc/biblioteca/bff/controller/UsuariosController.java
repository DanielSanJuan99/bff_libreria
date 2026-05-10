package cl.duoc.biblioteca.bff.controller;

import java.util.LinkedHashMap;
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
import cl.duoc.biblioteca.bff.dto.UsuarioDto;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController {

    private static final String EVENT_USUARIO_ELIMINACION = "Usuario.EliminacionSolicitada";

    private final FunctionsGatewayClient functionsGatewayClient;
    private final EventPublisherClient eventPublisherClient;

    /**
     * Crea el controlador de usuarios.
     *
     * @param functionsGatewayClient cliente gateway de tipo {@link FunctionsGatewayClient}.
     * @param eventPublisherClient cliente publisher de eventos de tipo {@link EventPublisherClient}.
     */
    public UsuariosController(FunctionsGatewayClient functionsGatewayClient,
                              EventPublisherClient eventPublisherClient) {
        this.functionsGatewayClient = functionsGatewayClient;
        this.eventPublisherClient = eventPublisherClient;
    }

    /**
     * Lista todos los usuarios.
     *
     * @return respuesta HTTP en {@link ResponseEntity} con {@link List} de {@link UsuarioDto}.
     */
    @GetMapping
    public ResponseEntity<List<UsuarioDto>> listar() {
        return ResponseEntity.ok(functionsGatewayClient.getUsuarios());
    }

    /**
     * Obtiene un usuario por su identificador.
     *
     * @param id identificador del usuario en {@link String}.
     * @return respuesta HTTP en {@link ResponseEntity} con un {@link UsuarioDto}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> obtener(@PathVariable String id) {
        return ResponseEntity.ok(functionsGatewayClient.getUsuarioById(id));
    }

    /**
     * Crea un nuevo usuario.
     *
     * @param request datos del usuario en {@link UsuarioDto}.
     * @return respuesta HTTP en {@link ResponseEntity} con el {@link UsuarioDto} creado.
     */
    @PostMapping
    public ResponseEntity<UsuarioDto> crear(@Valid @RequestBody UsuarioDto request) {
        return ResponseEntity.ok(functionsGatewayClient.createUsuario(request));
    }

    /**
     * Actualiza un usuario existente.
     *
     * @param id identificador del usuario en {@link String}.
     * @param request datos actualizados en {@link UsuarioDto}.
     * @return respuesta HTTP en {@link ResponseEntity} con el {@link UsuarioDto} actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> actualizar(@PathVariable String id, @Valid @RequestBody UsuarioDto request) {
        return ResponseEntity.ok(functionsGatewayClient.updateUsuario(id, request));
    }

    /**
     * Solicita la eliminacion de un usuario y publica {@code Usuario.EliminacionSolicitada}.
     * La function REST responde 202 con el snapshot de prestamos del usuario;
     * el consumer {@code reglasNegocioConsumer} hace la cascada (devolver copias
     * al stock, cancelar y borrar prestamos, eliminar usuario).
     *
     * @param id identificador del usuario en {@link String}.
     * @return respuesta HTTP en {@link ResponseEntity} con resultado en {@link Map}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable String id) {
        Map<String, Object> response = functionsGatewayClient.deleteUsuario(id);
        if (response != null && response.containsKey("idUsuario")) {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("idUsuario", id);
            data.put("usuario", response.get("usuario"));
            data.put("prestamos", response.get("prestamos"));
            data.put("totalPrestamos", response.get("totalPrestamos"));
            eventPublisherClient.publishAsync(
                    EVENT_USUARIO_ELIMINACION,
                    "biblioteca/usuarios/" + id,
                    data);
        }
        return ResponseEntity.ok(response);
    }
}
