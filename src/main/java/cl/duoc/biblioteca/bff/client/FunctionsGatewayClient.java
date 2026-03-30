package cl.duoc.biblioteca.bff.client;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cl.duoc.biblioteca.bff.dto.AutorDto;
import cl.duoc.biblioteca.bff.dto.LibroDto;
import cl.duoc.biblioteca.bff.dto.PrestamoDto;
import cl.duoc.biblioteca.bff.dto.UsuarioDto;

@Component
public class FunctionsGatewayClient {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final RestClient restClient;

    public FunctionsGatewayClient(RestClient functionsRestClient) {
        this.restClient = functionsRestClient;
    }

    public List<UsuarioDto> getUsuarios() {
        String response = restClient.get()
                .uri("/usuarios")
                .retrieve()
                .body(String.class);
        return parseResponse(response, new TypeReference<>() {
        });
    }

    public UsuarioDto getUsuarioById(String id) {
        String response = restClient.get()
                .uri("/usuarios/{id}", id)
                .retrieve()
                .body(String.class);
        return parseResponse(response, UsuarioDto.class);
    }

    public UsuarioDto createUsuario(UsuarioDto request) {
        String response = restClient.post()
                .uri("/usuarios")
                .body(request)
                .retrieve()
                .body(String.class);
        return parseResponse(response, UsuarioDto.class);
    }

    public UsuarioDto updateUsuario(String id, UsuarioDto request) {
        String response = restClient.put()
                .uri("/usuarios/{id}", id)
                .body(request)
                .retrieve()
                .body(String.class);
        return parseResponse(response, UsuarioDto.class);
    }

    public Map<String, Object> deleteUsuario(String id) {
        String response = restClient.delete()
                .uri("/usuarios/{id}", id)
                .retrieve()
                .body(String.class);
        return parseResponse(response, new TypeReference<>() {
        });
    }

    public List<PrestamoDto> getPrestamos() {
        String response = restClient.get()
                .uri("/prestamos")
                .retrieve()
                .body(String.class);
        return parseResponse(response, new TypeReference<>() {
        });
    }

    public PrestamoDto getPrestamoById(String id) {
        String response = restClient.get()
                .uri("/prestamos/{id}", id)
                .retrieve()
                .body(String.class);
        return parseResponse(response, PrestamoDto.class);
    }

    public PrestamoDto createPrestamo(PrestamoDto request) {
        String response = restClient.post()
                .uri("/prestamos")
                .body(request)
                .retrieve()
                .body(String.class);
        return parseResponse(response, PrestamoDto.class);
    }

    public PrestamoDto updatePrestamo(String id, PrestamoDto request) {
        String response = restClient.put()
                .uri("/prestamos/{id}", id)
                .body(request)
                .retrieve()
                .body(String.class);
        return parseResponse(response, PrestamoDto.class);
    }

    public Map<String, Object> deletePrestamo(String id) {
        String response = restClient.delete()
                .uri("/prestamos/{id}", id)
                .retrieve()
                .body(String.class);
        return parseResponse(response, new TypeReference<>() {
        });
    }

    public List<LibroDto> getLibros() {
        String response = restClient.get()
                .uri("/libros")
                .retrieve()
                .body(String.class);
        return parseResponse(response, new TypeReference<>() {
        });
    }

    public LibroDto getLibroById(String id) {
        String response = restClient.get()
                .uri("/libros/{id}", id)
                .retrieve()
                .body(String.class);
        return parseResponse(response, LibroDto.class);
    }

    public LibroDto createLibro(LibroDto request) {
        String response = restClient.post()
                .uri("/libros")
                .body(request)
                .retrieve()
                .body(String.class);
        return parseResponse(response, LibroDto.class);
    }

    public LibroDto updateLibro(String id, LibroDto request) {
        String response = restClient.put()
                .uri("/libros/{id}", id)
                .body(request)
                .retrieve()
                .body(String.class);
        return parseResponse(response, LibroDto.class);
    }

    public Map<String, Object> deleteLibro(String id) {
        String response = restClient.delete()
                .uri("/libros/{id}", id)
                .retrieve()
                .body(String.class);
        return parseResponse(response, new TypeReference<>() {
        });
    }

    public List<AutorDto> getAutores() {
        String response = restClient.get()
                .uri("/autores")
                .retrieve()
                .body(String.class);
        return parseResponse(response, new TypeReference<>() {
        });
    }

    public AutorDto getAutorById(String id) {
        String response = restClient.get()
                .uri("/autores/{id}", id)
                .retrieve()
                .body(String.class);
        return parseResponse(response, AutorDto.class);
    }

    public AutorDto createAutor(AutorDto request) {
        String response = restClient.post()
                .uri("/autores")
                .body(request)
                .retrieve()
                .body(String.class);
        return parseResponse(response, AutorDto.class);
    }

    public AutorDto updateAutor(String id, AutorDto request) {
        String response = restClient.put()
                .uri("/autores/{id}", id)
                .body(request)
                .retrieve()
                .body(String.class);
        return parseResponse(response, AutorDto.class);
    }

    public Map<String, Object> deleteAutor(String id) {
        String response = restClient.delete()
                .uri("/autores/{id}", id)
                .retrieve()
                .body(String.class);
        return parseResponse(response, new TypeReference<>() {
        });
    }

    private <T> T parseResponse(String response, Class<T> responseType) {
        try {
            return OBJECT_MAPPER.readValue(response, responseType);
        } catch (Exception ex) {
            throw new IllegalStateException("Error parseando respuesta de Functions", ex);
        }
    }

    private <T> T parseResponse(String response, TypeReference<T> responseType) {
        try {
            return OBJECT_MAPPER.readValue(response, responseType);
        } catch (Exception ex) {
            throw new IllegalStateException("Error parseando respuesta de Functions", ex);
        }
    }
}
