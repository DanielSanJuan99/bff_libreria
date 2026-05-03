package cl.duoc.biblioteca.bff.dto;

public record NotificacionDto(
        String id,
        String idUsuario,
        String tipo,
        String asunto,
        String cuerpo,
        String estado,
        String fechaCreacion,
        String fechaEnvio
) {
}
