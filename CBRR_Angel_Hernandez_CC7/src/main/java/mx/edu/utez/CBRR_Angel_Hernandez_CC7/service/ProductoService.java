package mx.edu.utez.CBRR_Angel_Hernandez_CC7.service;

import mx.edu.utez.CBRR_Angel_Hernandez_CC7.controller.dto.PedidoResponseDTO;
import mx.edu.utez.CBRR_Angel_Hernandez_CC7.controller.dto.ProductoRequestDTO;
import mx.edu.utez.CBRR_Angel_Hernandez_CC7.controller.dto.ProductoResponseDTO;
import mx.edu.utez.CBRR_Angel_Hernandez_CC7.exception.RecursoNoEncontradoException;
import mx.edu.utez.CBRR_Angel_Hernandez_CC7.exception.ReglaNegocioException;
import mx.edu.utez.CBRR_Angel_Hernandez_CC7.model.EstadoPedido;
import mx.edu.utez.CBRR_Angel_Hernandez_CC7.model.Pedido;
import mx.edu.utez.CBRR_Angel_Hernandez_CC7.model.Producto;
import mx.edu.utez.CBRR_Angel_Hernandez_CC7.repository.PedidoRepository;
import mx.edu.utez.CBRR_Angel_Hernandez_CC7.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final PedidoRepository pedidoRepository;

    public ProductoService(ProductoRepository productoRepository, PedidoRepository pedidoRepository) {
        this.productoRepository = productoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public ProductoResponseDTO crear(ProductoRequestDTO dto) {
        Producto producto = new Producto();
        producto.setNombre(dto.nombre().trim());
        producto.setCategoria(dto.categoria().trim());
        producto.setPrecio(dto.precio());
        producto.setStock(dto.stock());
        producto.setActivo(true);
        return aDTO(productoRepository.save(producto));
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarTodos() {
        return productoRepository.findAll().stream().map(ProductoService::aDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarActivos() {
        return productoRepository.findByActivoTrue().stream().map(ProductoService::aDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarPorCategoria(String categoria) {
        return productoRepository.findByCategoriaIgnoreCase(categoria.trim()).stream()
                .map(ProductoService::aDTO).toList();
    }

    @Transactional
    public ProductoResponseDTO desactivar(Long id) {
        Producto producto = buscarPorId(id);
        if (pedidoRepository.existsByProductoIdAndEstado(id, EstadoPedido.PENDIENTE)) {
            throw new ReglaNegocioException("No se puede desactivar un producto con pedidos PENDIENTES");
        }
        producto.setActivo(false);
        return aDTO(productoRepository.save(producto));
    }

    @Transactional
    public List<PedidoResponseDTO> cancelarPedidosPendientes(Long id) {
        Producto producto = buscarPorId(id);
        List<Pedido> pendientes = pedidoRepository.findByProductoIdAndEstado(id, EstadoPedido.PENDIENTE);
        int devolver = 0;
        for (Pedido pedido : pendientes) {
            pedido.setEstado(EstadoPedido.CANCELADO);
            devolver += pedido.getCantidad();
        }
        producto.setStock(producto.getStock() + devolver);
        productoRepository.save(producto);
        return pedidoRepository.saveAll(pendientes).stream().map(PedidoService::aDTO).toList();
    }

    private Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un producto con id " + id));
    }

    public static ProductoResponseDTO aDTO(Producto p) {
        return new ProductoResponseDTO(p.getId(), p.getNombre(), p.getCategoria(),
                p.getPrecio(), p.getStock(), p.isActivo());
    }
}
