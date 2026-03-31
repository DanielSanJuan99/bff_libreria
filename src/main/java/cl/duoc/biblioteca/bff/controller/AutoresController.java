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

import cl.duoc.biblioteca.bff.client.FunctionsGatewayClient;
import cl.duoc.biblioteca.bff.dto.AutorDto;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/autores")
public class AutoresController {

    private final FunctionsGatewayClient functionsGatewayClient;

    /**
     * Crea el controlador de autores.
     *
     * @param functionsGatewayClient cliente gateway de tipo {@link FunctionsGatewayClient}.
     */
    public AutoresController(FunctionsGatewayClient functionsGatewayClient) {
        this.functionsGatewayClient = functionsGatewayClient;
    }

    /**
     * Lista todos los autores.
     *
     * @return respuesta HTTP en {@link ResponseEntity} con {@link List} de {@link AutorDto}.
     */
    @GetMapping
    public ResponseEntity<List<AutorDto>> listar() {
        return ResponseEntity.ok(functionsGatewayClient.getAutores());
    }

    /**
     * Obtiene un autor por su identificador.
     *
     * @param id identificador del autor en {@link String}.
     * @return respuesta HTTP en {@link ResponseEntity} con un {@link AutorDto}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AutorDto> obtener(@PathVariable String id) {
        return ResponseEntity.ok(functionsGatewayClient.getAutorById(id));
    }

    /**
     * Crea un nuevo autor.
     *
     * @param request datos del autor en {@link AutorDto}.
     * @return respuesta HTTP en {@link ResponseEntity} con el {@link AutorDto} creado.
     */
    @PostMapping
    public ResponseEntity<AutorDto> crear(@Valid @RequestBody AutorDto request) {
        return ResponseEntity.ok(functionsGatewayClient.createAutor(request));
    }

    /**
     * Actualiza un autor existente.
     *
     * @param id identificador del autor en {@link String}.
     * @param request datos actualizados en {@link AutorDto}.
     * @return respuesta HTTP en {@link ResponseEntity} con el {@link AutorDto} actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AutorDto> actualizar(@PathVariable String id, @Valid @RequestBody AutorDto request) {
        return ResponseEntity.ok(functionsGatewayClient.updateAutor(id, request));
    }

    /**
     * Elimina un autor por su identificador.
     *
     * @param id identificador del autor en {@link String}.
     * @return respuesta HTTP en {@link ResponseEntity} con resultado en {@link Map}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable String id) {
        return ResponseEntity.ok(functionsGatewayClient.deleteAutor(id));
    }
}
