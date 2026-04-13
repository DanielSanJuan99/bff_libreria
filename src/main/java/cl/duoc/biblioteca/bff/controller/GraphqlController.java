package cl.duoc.biblioteca.bff.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.biblioteca.bff.client.FunctionsGatewayClient;

@RestController
@RequestMapping("/api/graphql")
public class GraphqlController {

    private final FunctionsGatewayClient functionsGatewayClient;

    /**
     * Crea el controlador de endpoints GraphQL.
     *
     * @param functionsGatewayClient cliente gateway de tipo {@link FunctionsGatewayClient}.
     */
    public GraphqlController(FunctionsGatewayClient functionsGatewayClient) {
        this.functionsGatewayClient = functionsGatewayClient;
    }

    /**
     * Reenvía peticiones GraphQL del dominio catálogo.
     *
     * @param request body GraphQL en {@link Map}.
     * @return respuesta HTTP en {@link ResponseEntity} con resultado en {@link Map}.
     */
    @PostMapping("/catalogo")
    public ResponseEntity<Map<String, Object>> graphqlCatalogo(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(functionsGatewayClient.postGraphqlCatalogo(request));
    }

    /**
     * Reenvía peticiones GraphQL del dominio general.
     *
     * @param request body GraphQL en {@link Map}.
     * @return respuesta HTTP en {@link ResponseEntity} con resultado en {@link Map}.
     */
    @PostMapping("/general")
    public ResponseEntity<Map<String, Object>> graphqlGeneral(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(functionsGatewayClient.postGraphqlGeneral(request));
    }
}
