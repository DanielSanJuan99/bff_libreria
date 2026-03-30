package cl.duoc.biblioteca.bff.dto;

import jakarta.validation.constraints.NotBlank;

public record PrestamoDto(
        String id,
        @NotBlank(message = "El idUsuario es obligatorio")
        String idUsuario,
        @NotBlank(message = "El idLibro es obligatorio")
        String idLibro,
        String fechaPrestamo,
        String fechaDevolucion,
        String estado
) {
}
