package mx.edu.utez.CBRR_Angel_Hernandez_CC7.service;

import mx.edu.utez.CBRR_Angel_Hernandez_CC7.controller.dto.PedidoRequestDTO;
import mx.edu.utez.CBRR_Angel_Hernandez_CC7.controller.dto.PedidoResponseDTO;
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
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    public PedidoResponseDTO crear(PedidoRequestDTO dto) {
        Producto producto = productoRepository.findById(dto.productoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un producto con id " + dto.productoId()));
        if (!producto.isActivo()) {
            throw new ReglaNegocioException("El producto no está activo");
        }
        if (producto.getStock() < dto.cantidad()) {
            throw new ReglaNegocioException("Stock insuficiente. Disponible: " + producto.getStock());
        }
        Pedido pedido = new Pedido();
        pedido.setNombreCliente(dto.nombreCliente().trim());
        pedido.setCantidad(dto.cantidad());
        pedido.setPrecioUnitario(producto.getPrecio());
        pedido.setTotal(producto.getPrecio() * dto.cantidad());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setProducto(producto);
        producto.setStock(producto.getStock() - dto.cantidad());
        productoRepository.save(producto);
        return aDTO(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponseDTO cancelar(Long id) {
        Pedido pedido = buscarPorId(id);
        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new ReglaNegocioException("Solo se pueden cancelar pedidos en estado PENDIENTE");
        }
        Producto producto = pedido.getProducto();
        producto.setStock(producto.getStock() + pedido.getCantidad());
        pedido.setEstado(EstadoPedido.CANCELADO);
        productoRepository.save(producto);
        return aDTO(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponseDTO procesar(Long id) {
        Pedido pedido = buscarPorId(id);
        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new ReglaNegocioException("Solo se pueden procesar pedidos en estado PENDIENTE");
        }
        pedido.setEstado(EstadoPedido.PROCESADO);
        return aDTO(pedidoRepository.save(pedido));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarPorEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstado(estado).stream().map(PedidoService::aDTO).toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO detalle(Long id) {
        return aDTO(buscarPorId(id));
    }

    private Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un pedido con id " + id));
    }

    public static PedidoResponseDTO aDTO(Pedido p) {
        return new PedidoResponseDTO(p.getId(), p.getNombreCliente(), p.getCantidad(),
                p.getPrecioUnitario(), p.getTotal(), p.getEstado(), ProductoService.aDTO(p.getProducto()));
    }
}
