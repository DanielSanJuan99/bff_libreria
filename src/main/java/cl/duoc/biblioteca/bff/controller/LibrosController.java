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

    public LibrosController(FunctionsGatewayClient functionsGatewayClient) {
        this.functionsGatewayClient = functionsGatewayClient;
    }

    @GetMapping
    public ResponseEntity<List<LibroDto>> listar() {
        return ResponseEntity.ok(functionsGatewayClient.getLibros());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroDto> obtener(@PathVariable String id) {
        return ResponseEntity.ok(functionsGatewayClient.getLibroById(id));
    }

    @PostMapping
    public ResponseEntity<LibroDto> crear(@Valid @RequestBody LibroDto request) {
        return ResponseEntity.ok(functionsGatewayClient.createLibro(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroDto> actualizar(@PathVariable String id, @Valid @RequestBody LibroDto request) {
        return ResponseEntity.ok(functionsGatewayClient.updateLibro(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable String id) {
        return ResponseEntity.ok(functionsGatewayClient.deleteLibro(id));
    }
}
