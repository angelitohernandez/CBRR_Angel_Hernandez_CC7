package mx.edu.utez.CBRR_Angel_Hernandez_CC7.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductoRequestDTO(
        @NotBlank(message = "El nombre no puede estar vacío") String nombre,
        @NotBlank(message = "La categoría no puede estar vacía") String categoria,
        @NotNull(message = "El precio es obligatorio")
        @Positive(message = "El precio debe ser mayor a cero") Double precio,
        @NotNull(message = "El stock es obligatorio")
        @PositiveOrZero(message = "El stock no puede ser negativo") Integer stock
) {
}
