package mx.edu.utez.CBRR_Angel_Hernandez_CC7.controller;

import jakarta.validation.Valid;
import mx.edu.utez.CBRR_Angel_Hernandez_CC7.controller.dto.PedidoRequestDTO;
import mx.edu.utez.CBRR_Angel_Hernandez_CC7.controller.dto.PedidoResponseDTO;
import mx.edu.utez.CBRR_Angel_Hernandez_CC7.model.EstadoPedido;
import mx.edu.utez.CBRR_Angel_Hernandez_CC7.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> crear(@Valid @RequestBody PedidoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crear(dto));
    }

    @GetMapping("/estado/{estado}")
    public List<PedidoResponseDTO> listarPorEstado(@PathVariable EstadoPedido estado) {
        return pedidoService.listarPorEstado(estado);
    }

    @GetMapping("/{id}")
    public PedidoResponseDTO detalle(@PathVariable Long id) {
        return pedidoService.detalle(id);
    }

    @PatchMapping("/{id}/cancelar")
    public PedidoResponseDTO cancelar(@PathVariable Long id) {
        return pedidoService.cancelar(id);
    }

    @PatchMapping("/{id}/procesar")
    public PedidoResponseDTO procesar(@PathVariable Long id) {
        return pedidoService.procesar(id);
    }
}
