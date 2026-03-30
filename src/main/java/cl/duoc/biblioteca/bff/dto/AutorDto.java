package cl.duoc.biblioteca.bff.dto;

import jakarta.validation.constraints.NotBlank;

public record AutorDto(
        String id,
        @NotBlank(message = "El nombre del autor es obligatorio")
        String nombreAutor
) {
}
