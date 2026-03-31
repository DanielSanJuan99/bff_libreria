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
import cl.duoc.biblioteca.bff.dto.LibroDto;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/libros")
public class LibrosController {

    private final FunctionsGatewayClient functionsGatewayClient;

    /**
     * Crea el controlador de libros.
     *
     * @param functionsGatewayClient cliente gateway de tipo {@link FunctionsGatewayClient}.
     */
    public LibrosController(FunctionsGatewayClient functionsGatewayClient) {
        this.functionsGatewayClient = functionsGatewayClient;
    }

    /**
     * Lista todos los libros.
     *
     * @return respuesta HTTP en {@link ResponseEntity} con {@link List} de {@link LibroDto}.
     */
    @GetMapping
    public ResponseEntity<List<LibroDto>> listar() {
        return ResponseEntity.ok(functionsGatewayClient.getLibros());
    }

    /**
     * Obtiene un libro por su identificador.
     *
     * @param id identificador del libro en {@link String}.
     * @return respuesta HTTP en {@link ResponseEntity} con un {@link LibroDto}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LibroDto> obtener(@PathVariable String id) {
        return ResponseEntity.ok(functionsGatewayClient.getLibroById(id));
    }

    /**
     * Crea un nuevo libro.
     *
     * @param request datos del libro en {@link LibroDto}.
     * @return respuesta HTTP en {@link ResponseEntity} con el {@link LibroDto} creado.
     */
    @PostMapping
    public ResponseEntity<LibroDto> crear(@Valid @RequestBody LibroDto request) {
        return ResponseEntity.ok(functionsGatewayClient.createLibro(request));
    }

    /**
     * Actualiza un libro existente.
     *
     * @param id identificador del libro en {@link String}.
     * @param request datos actualizados en {@link LibroDto}.
     * @return respuesta HTTP en {@link ResponseEntity} con el {@link LibroDto} actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LibroDto> actualizar(@PathVariable String id, @Valid @RequestBody LibroDto request) {
        return ResponseEntity.ok(functionsGatewayClient.updateLibro(id, request));
    }

    /**
     * Elimina un libro por su identificador.
     *
     * @param id identificador del libro en {@link String}.
     * @return respuesta HTTP en {@link ResponseEntity} con resultado en {@link Map}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable String id) {
        return ResponseEntity.ok(functionsGatewayClient.deleteLibro(id));
    }
}
