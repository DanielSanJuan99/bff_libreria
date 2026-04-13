package cl.duoc.biblioteca.bff.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.biblioteca.bff.client.FunctionsGatewayClient;

@RestController
@RequestMapping("/api/resumen")
public class ResumenController {

    private final FunctionsGatewayClient functionsGatewayClient;

    /**
     * Crea el controlador de resúmenes.
     *
     * @param functionsGatewayClient cliente gateway de tipo {@link FunctionsGatewayClient}.
     */
    public ResumenController(FunctionsGatewayClient functionsGatewayClient) {
        this.functionsGatewayClient = functionsGatewayClient;
    }

    /**
     * Obtiene resumen de catálogo.
     *
     * @return respuesta HTTP en {@link ResponseEntity} con resultado en {@link Map}.
     */
    @GetMapping("/catalogo")
    public ResponseEntity<Map<String, Object>> resumenCatalogo() {
        return ResponseEntity.ok(functionsGatewayClient.getResumenCatalogo());
    }

    /**
     * Obtiene resumen general de usuarios y préstamos.
     *
     * @return respuesta HTTP en {@link ResponseEntity} con resultado en {@link Map}.
     */
    @GetMapping("/general")
    public ResponseEntity<Map<String, Object>> resumenGeneral() {
        return ResponseEntity.ok(functionsGatewayClient.getResumenGeneral());
    }
}
