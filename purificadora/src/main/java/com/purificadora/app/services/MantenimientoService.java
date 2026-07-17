package com.purificadora.app.services;

import com.purificadora.app.models.Mantenimiento;
import com.purificadora.app.models.Producto;
import com.purificadora.app.repositories.MantenimientoRepository;
import com.purificadora.app.repositories.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MantenimientoService {

    private final MantenimientoRepository mantenimientoRepository;
    private final ProductoRepository productoRepository;

    public MantenimientoService(MantenimientoRepository mantenimientoRepository, ProductoRepository productoRepository) {
        this.mantenimientoRepository = mantenimientoRepository;
        this.productoRepository = productoRepository;
    }

    public List<Mantenimiento> listarTodos() {
        return mantenimientoRepository.findAll();
    }

    public Optional<Mantenimiento> buscarPorId(Long id) {
        return mantenimientoRepository.findById(id);
    }

    @Transactional
    public Mantenimiento guardar(Mantenimiento mantenimiento) {
        // Si no se proporcionó fecha realizada, usar ahora
        if (mantenimiento.getFechaRealizado() == null) {
            mantenimiento.setFechaRealizado(LocalDateTime.now());
        }

        // Si se indicó un producto, decrementar stock en 1 unidad (sin bajar de 0)
        if (mantenimiento.getProducto() != null && mantenimiento.getProducto().getIdProducto() != null) {
            Long idProd = mantenimiento.getProducto().getIdProducto();
            productoRepository.findById(idProd).ifPresent(prod -> {
                Integer current = prod.getStockActual() != null ? prod.getStockActual() : 0;
                if (current > 0) {
                    prod.setStockActual(current - 1);
                }
                productoRepository.save(prod);
                // asegurarnos de que la entidad asociada esté sincronizada
                mantenimiento.setProducto(prod);
            });
        }

        return mantenimientoRepository.save(mantenimiento);
    }

    @Transactional
    public void eliminar(Long id) {
        mantenimientoRepository.deleteById(id);
    }

    public List<Mantenimiento> obtenerUrgentes() {
        return mantenimientoRepository.findByProximaFechaLessThanEqual(LocalDate.now());
    }
}
