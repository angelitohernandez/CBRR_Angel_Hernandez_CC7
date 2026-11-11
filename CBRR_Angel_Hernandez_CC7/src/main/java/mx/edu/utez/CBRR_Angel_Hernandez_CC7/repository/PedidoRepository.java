package mx.edu.utez.CBRR_Angel_Hernandez_CC7.repository;

import mx.edu.utez.CBRR_Angel_Hernandez_CC7.model.EstadoPedido;
import mx.edu.utez.CBRR_Angel_Hernandez_CC7.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByEstado(EstadoPedido estado);
    List<Pedido> findByProductoIdAndEstado(Long productoId, EstadoPedido estado);
    boolean existsByProductoIdAndEstado(Long productoId, EstadoPedido estado);
}
