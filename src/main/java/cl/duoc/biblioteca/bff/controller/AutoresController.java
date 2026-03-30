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

    public AutoresController(FunctionsGatewayClient functionsGatewayClient) {
        this.functionsGatewayClient = functionsGatewayClient;
    }

    @GetMapping
    public ResponseEntity<List<AutorDto>> listar() {
        return ResponseEntity.ok(functionsGatewayClient.getAutores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorDto> obtener(@PathVariable String id) {
        return ResponseEntity.ok(functionsGatewayClient.getAutorById(id));
    }

    @PostMapping
    public ResponseEntity<AutorDto> crear(@Valid @RequestBody AutorDto request) {
        return ResponseEntity.ok(functionsGatewayClient.createAutor(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorDto> actualizar(@PathVariable String id, @Valid @RequestBody AutorDto request) {
        return ResponseEntity.ok(functionsGatewayClient.updateAutor(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable String id) {
        return ResponseEntity.ok(functionsGatewayClient.deleteAutor(id));
    }
}
