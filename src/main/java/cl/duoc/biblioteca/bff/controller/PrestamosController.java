package cl.duoc.biblioteca.bff.controller;

import cl.duoc.biblioteca.bff.client.FunctionsGatewayClient;
import cl.duoc.biblioteca.bff.dto.PrestamoDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamosController {

    private final FunctionsGatewayClient functionsGatewayClient;

    public PrestamosController(FunctionsGatewayClient functionsGatewayClient) {
        this.functionsGatewayClient = functionsGatewayClient;
    }

    @GetMapping
    public ResponseEntity<List<PrestamoDto>> listar() {
        return ResponseEntity.ok(functionsGatewayClient.getPrestamos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoDto> obtener(@PathVariable String id) {
        return ResponseEntity.ok(functionsGatewayClient.getPrestamoById(id));
    }

    @PostMapping
    public ResponseEntity<PrestamoDto> crear(@Valid @RequestBody PrestamoDto request) {
        return ResponseEntity.ok(functionsGatewayClient.createPrestamo(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrestamoDto> actualizar(@PathVariable String id, @Valid @RequestBody PrestamoDto request) {
        return ResponseEntity.ok(functionsGatewayClient.updatePrestamo(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable String id) {
        return ResponseEntity.ok(functionsGatewayClient.deletePrestamo(id));
    }
}
