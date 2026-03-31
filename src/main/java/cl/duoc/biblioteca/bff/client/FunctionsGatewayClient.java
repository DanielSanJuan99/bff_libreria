package cl.duoc.biblioteca.bff.client;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
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

    /**
     * Crea el cliente gateway para consumir Azure Functions.
     *
     * @param functionsRestClient cliente HTTP de tipo {@link RestClient}.
     */
    public FunctionsGatewayClient(RestClient functionsRestClient) {
        this.restClient = functionsRestClient;
    }

    /**
     * Obtiene la lista de usuarios.
     *
     * @return listado en {@link List} de {@link UsuarioDto}.
     */
    public List<UsuarioDto> getUsuarios() {
        String response = restClient.get()
                .uri("/usuarios")
                .retrieve()
                .body(String.class);
        return parseResponse(response, new TypeReference<>() {
        });
    }

    /**
     * Obtiene un usuario por su identificador.
     *
     * @param id identificador del usuario en {@link String}.
     * @return usuario encontrado de tipo {@link UsuarioDto}.
     */
    public UsuarioDto getUsuarioById(String id) {
        String response = restClient.get()
                .uri("/usuarios/{id}", id)
                .retrieve()
                .body(String.class);
        return parseResponse(response, UsuarioDto.class);
    }

    /**
     * Crea un nuevo usuario.
     *
     * @param request datos del usuario en {@link UsuarioDto}.
     * @return usuario creado de tipo {@link UsuarioDto}.
     */
    public UsuarioDto createUsuario(UsuarioDto request) {
        String requestBody = toJson(request);
        String response = restClient.post()
                .uri("/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody)
                .retrieve()
                .body(String.class);
        return parseResponse(response, UsuarioDto.class);
    }

    /**
     * Actualiza un usuario existente.
     *
     * @param id identificador del usuario en {@link String}.
     * @param request datos actualizados en {@link UsuarioDto}.
     * @return usuario actualizado de tipo {@link UsuarioDto}.
     */
    public UsuarioDto updateUsuario(String id, UsuarioDto request) {
        String requestBody = toJson(request);
        String response = restClient.put()
                .uri("/usuarios/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody)
                .retrieve()
                .body(String.class);
        return parseResponse(response, UsuarioDto.class);
    }

    /**
     * Elimina un usuario por identificador.
     *
     * @param id identificador del usuario en {@link String}.
     * @return resultado de la operación en {@link Map}.
     */
    public Map<String, Object> deleteUsuario(String id) {
        String response = restClient.delete()
                .uri("/usuarios/{id}", id)
                .retrieve()
                .body(String.class);
        return parseResponse(response, new TypeReference<>() {
        });
    }

    /**
     * Obtiene la lista de préstamos.
     *
     * @return listado en {@link List} de {@link PrestamoDto}.
     */
    public List<PrestamoDto> getPrestamos() {
        String response = restClient.get()
                .uri("/prestamos")
                .retrieve()
                .body(String.class);
        return parseResponse(response, new TypeReference<>() {
        });
    }

    /**
     * Obtiene un préstamo por su identificador.
     *
     * @param id identificador del préstamo en {@link String}.
     * @return préstamo encontrado de tipo {@link PrestamoDto}.
     */
    public PrestamoDto getPrestamoById(String id) {
        String response = restClient.get()
                .uri("/prestamos/{id}", id)
                .retrieve()
                .body(String.class);
        return parseResponse(response, PrestamoDto.class);
    }

    /**
     * Crea un nuevo préstamo.
     *
     * @param request datos del préstamo en {@link PrestamoDto}.
     * @return préstamo creado de tipo {@link PrestamoDto}.
     */
    public PrestamoDto createPrestamo(PrestamoDto request) {
        String requestBody = toJson(request);
        String response = restClient.post()
                .uri("/prestamos")
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody)
                .retrieve()
                .body(String.class);
        return parseResponse(response, PrestamoDto.class);
    }

    /**
     * Actualiza un préstamo existente.
     *
     * @param id identificador del préstamo en {@link String}.
     * @param request datos actualizados en {@link PrestamoDto}.
     * @return préstamo actualizado de tipo {@link PrestamoDto}.
     */
    public PrestamoDto updatePrestamo(String id, PrestamoDto request) {
        String requestBody = toJson(request);
        String response = restClient.put()
                .uri("/prestamos/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody)
                .retrieve()
                .body(String.class);
        return parseResponse(response, PrestamoDto.class);
    }

    /**
     * Elimina un préstamo por identificador.
     *
     * @param id identificador del préstamo en {@link String}.
     * @return resultado de la operación en {@link Map}.
     */
    public Map<String, Object> deletePrestamo(String id) {
        String response = restClient.delete()
                .uri("/prestamos/{id}", id)
                .retrieve()
                .body(String.class);
        return parseResponse(response, new TypeReference<>() {
        });
    }

    /**
     * Obtiene la lista de libros.
     *
     * @return listado en {@link List} de {@link LibroDto}.
     */
    public List<LibroDto> getLibros() {
        String response = restClient.get()
                .uri("/libros")
                .retrieve()
                .body(String.class);
        return parseResponse(response, new TypeReference<>() {
        });
    }

    /**
     * Obtiene un libro por su identificador.
     *
     * @param id identificador del libro en {@link String}.
     * @return libro encontrado de tipo {@link LibroDto}.
     */
    public LibroDto getLibroById(String id) {
        String response = restClient.get()
                .uri("/libros/{id}", id)
                .retrieve()
                .body(String.class);
        return parseResponse(response, LibroDto.class);
    }

    /**
     * Crea un nuevo libro.
     *
     * @param request datos del libro en {@link LibroDto}.
     * @return libro creado de tipo {@link LibroDto}.
     */
    public LibroDto createLibro(LibroDto request) {
        String requestBody = toJson(request);
        String response = restClient.post()
                .uri("/libros")
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody)
                .retrieve()
                .body(String.class);
        return parseResponse(response, LibroDto.class);
    }

    /**
     * Actualiza un libro existente.
     *
     * @param id identificador del libro en {@link String}.
     * @param request datos actualizados en {@link LibroDto}.
     * @return libro actualizado de tipo {@link LibroDto}.
     */
    public LibroDto updateLibro(String id, LibroDto request) {
        String requestBody = toJson(request);
        String response = restClient.put()
                .uri("/libros/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody)
                .retrieve()
                .body(String.class);
        return parseResponse(response, LibroDto.class);
    }

    /**
     * Elimina un libro por identificador.
     *
     * @param id identificador del libro en {@link String}.
     * @return resultado de la operación en {@link Map}.
     */
    public Map<String, Object> deleteLibro(String id) {
        String response = restClient.delete()
                .uri("/libros/{id}", id)
                .retrieve()
                .body(String.class);
        return parseResponse(response, new TypeReference<>() {
        });
    }

    /**
     * Obtiene la lista de autores.
     *
     * @return listado en {@link List} de {@link AutorDto}.
     */
    public List<AutorDto> getAutores() {
        String response = restClient.get()
                .uri("/autores")
                .retrieve()
                .body(String.class);
        return parseResponse(response, new TypeReference<>() {
        });
    }

    /**
     * Obtiene un autor por su identificador.
     *
     * @param id identificador del autor en {@link String}.
     * @return autor encontrado de tipo {@link AutorDto}.
     */
    public AutorDto getAutorById(String id) {
        String response = restClient.get()
                .uri("/autores/{id}", id)
                .retrieve()
                .body(String.class);
        return parseResponse(response, AutorDto.class);
    }

    /**
     * Crea un nuevo autor.
     *
     * @param request datos del autor en {@link AutorDto}.
     * @return autor creado de tipo {@link AutorDto}.
     */
    public AutorDto createAutor(AutorDto request) {
        String requestBody = toJson(request);
        String response = restClient.post()
                .uri("/autores")
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody)
                .retrieve()
                .body(String.class);
        return parseResponse(response, AutorDto.class);
    }

    /**
     * Actualiza un autor existente.
     *
     * @param id identificador del autor en {@link String}.
     * @param request datos actualizados en {@link AutorDto}.
     * @return autor actualizado de tipo {@link AutorDto}.
     */
    public AutorDto updateAutor(String id, AutorDto request) {
        String requestBody = toJson(request);
        String response = restClient.put()
                .uri("/autores/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(String.class);
        return parseResponse(response, AutorDto.class);
    }

    private String toJson(Object request) {
        try {
            return OBJECT_MAPPER.writeValueAsString(request);
        } catch (Exception ex) {
            throw new IllegalStateException("Error serializando request para Functions", ex);
        }
    }

    /**
     * Elimina un autor por identificador.
     *
     * @param id identificador del autor en {@link String}.
     * @return resultado de la operación en {@link Map}.
     */
    public Map<String, Object> deleteAutor(String id) {
        String response = restClient.delete()
                .uri("/autores/{id}", id)
                .retrieve()
                .body(String.class);
        return parseResponse(response, new TypeReference<>() {
        });
    }

    /**
     * Convierte una respuesta JSON a un tipo concreto de clase.
     *
     * @param response contenido JSON en {@link String}.
     * @param responseType tipo esperado en {@link Class}.
     * @param <T> tipo genérico de retorno en {@code T}.
     * @return objeto parseado de tipo {@code T}.
     */
    private <T> T parseResponse(String response, Class<T> responseType) {
        try {
            return OBJECT_MAPPER.readValue(response, responseType);
        } catch (Exception ex) {
            throw new IllegalStateException("Error parseando respuesta de Functions", ex);
        }
    }

    /**
     * Convierte una respuesta JSON usando un tipo genérico avanzado.
     *
     * @param response contenido JSON en {@link String}.
     * @param responseType referencia de tipo en {@link TypeReference}.
     * @param <T> tipo genérico de retorno en {@code T}.
     * @return objeto parseado de tipo {@code T}.
     */
    private <T> T parseResponse(String response, TypeReference<T> responseType) {
        try {
            return OBJECT_MAPPER.readValue(response, responseType);
        } catch (Exception ex) {
            throw new IllegalStateException("Error parseando respuesta de Functions", ex);
        }
    }
}
