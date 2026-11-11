package mx.edu.utez.CBRR_Angel_Hernandez_CC7.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PedidoRequestDTO(
        @NotNull(message = "El id del producto es obligatorio") Long productoId,
        @NotBlank(message = "El nombre del cliente no puede estar vacío") String nombreCliente,
        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor a cero") Integer cantidad
) {
}
