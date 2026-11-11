package mx.edu.utez.CBRR_Angel_Hernandez_CC7.controller;

import jakarta.validation.Valid;
import mx.edu.utez.CBRR_Angel_Hernandez_CC7.controller.dto.PedidoResponseDTO;
import mx.edu.utez.CBRR_Angel_Hernandez_CC7.controller.dto.ProductoRequestDTO;
import mx.edu.utez.CBRR_Angel_Hernandez_CC7.controller.dto.ProductoResponseDTO;
import mx.edu.utez.CBRR_Angel_Hernandez_CC7.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crear(@Valid @RequestBody ProductoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(dto));
    }

    @GetMapping
    public List<ProductoResponseDTO> listarTodos() {
        return productoService.listarTodos();
    }

    @GetMapping("/activos")
    public List<ProductoResponseDTO> listarActivos() {
        return productoService.listarActivos();
    }

    @GetMapping("/categoria/{categoria}")
    public List<ProductoResponseDTO> listarPorCategoria(@PathVariable String categoria) {
        return productoService.listarPorCategoria(categoria);
    }

    @PatchMapping("/{id}/desactivar")
    public ProductoResponseDTO desactivar(@PathVariable Long id) {
        return productoService.desactivar(id);
    }

    @PatchMapping("/{id}/cancelar-pendientes")
    public List<PedidoResponseDTO> cancelarPendientes(@PathVariable Long id) {
        return productoService.cancelarPedidosPendientes(id);
    }
}
