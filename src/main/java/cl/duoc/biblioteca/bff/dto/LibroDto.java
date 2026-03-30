package cl.duoc.biblioteca.bff.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LibroDto(
        String id,
        @NotBlank(message = "El ISBN es obligatorio")
        String isbn,
        @NotBlank(message = "El título es obligatorio")
        String titulo,
        @NotNull(message = "El año de publicación es obligatorio")
        Integer anioPublicacion,
        @NotNull(message = "Las copias totales son obligatorias")
        @Min(value = 0, message = "Las copias totales deben ser >= 0")
        Integer copiasTotales,
        @NotNull(message = "Las copias disponibles son obligatorias")
        @Min(value = 0, message = "Las copias disponibles deben ser >= 0")
        Integer copiasDisponible,
        @NotBlank(message = "El idAutor es obligatorio")
        String idAutor
) {
}
