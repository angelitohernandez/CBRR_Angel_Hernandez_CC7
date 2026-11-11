package mx.edu.utez.CBRR_Angel_Hernandez_CC7.repository;

import mx.edu.utez.CBRR_Angel_Hernandez_CC7.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoriaIgnoreCase(String categoria);
    List<Producto> findByActivoTrue();
}
