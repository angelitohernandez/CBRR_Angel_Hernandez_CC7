package mx.edu.utez.CBRR_Angel_Hernandez_CC7.controller.dto;

public record ProductoResponseDTO(
        Long id,
        String nombre,
        String categoria,
        Double precio,
        Integer stock,
        boolean activo
) {
}
