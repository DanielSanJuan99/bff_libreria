package cl.duoc.biblioteca.bff.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioDto(
        String id,
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,
        @NotBlank(message = "El apellido paterno es obligatorio")
        String apellidoPaterno,
        @NotBlank(message = "El apellido materno es obligatorio")
        String apellidoMaterno,
        @Email(message = "El email debe ser válido")
        @NotBlank(message = "El email es obligatorio")
        String email,
        Boolean activo
) {
}
