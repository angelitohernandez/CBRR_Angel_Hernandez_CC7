package mx.edu.utez.CBRR_Angel_Hernandez_CC7.controller.dto;

import mx.edu.utez.CBRR_Angel_Hernandez_CC7.model.EstadoPedido;

public record PedidoResponseDTO(
        Long id,
        String nombreCliente,
        Integer cantidad,
        Double precioUnitario,
        Double total,
        EstadoPedido estado,
        ProductoResponseDTO producto
) {
}
