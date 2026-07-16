package com.purificadora.app.services;

import com.purificadora.app.entities.Usuario;
import com.purificadora.app.models.Cliente;
import com.purificadora.app.models.DetalleVenta;
import com.purificadora.app.models.Producto;
import com.purificadora.app.models.TipoVenta;
import com.purificadora.app.models.Venta;
import com.purificadora.app.repositories.ClienteRepository;
import com.purificadora.app.repositories.ProductoRepository;
import com.purificadora.app.repositories.VentaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VentaServiceImpl implements VentaService {

    private final ClienteRepository clienteRepository;
    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;

    public VentaServiceImpl(ClienteRepository clienteRepository, VentaRepository ventaRepository, ProductoRepository productoRepository) {
        this.clienteRepository = clienteRepository;
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> buscarClientePorId(Long idCliente) {
        return clienteRepository.findById(idCliente);
    }

    @Override
    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminarCliente(Long idCliente) {
        clienteRepository.deleteById(idCliente);
    }

    @Override
    public Venta registrarVenta(Venta venta, List<DetalleVenta> detalles) {
        venta.setDetalles(detalles);
        detalles.forEach(detalle -> {
            detalle.setVenta(venta);
            Producto producto = productoRepository.findById(detalle.getProducto().getIdProducto())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
            int nuevoStock = producto.getStockActual() - detalle.getCantidad();
            if (nuevoStock < 0) {
                throw new IllegalArgumentException("Stock insuficiente para el producto: " + producto.getNombre());
            }
            producto.setStockActual(nuevoStock);
            productoRepository.save(producto);
        });
        return ventaRepository.save(venta);
    }

    @Override
    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public List<Venta> listarVentasPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaRepository.findByFechaVentaBetween(fechaInicio, fechaFin);
    }

    @Override
    public List<Venta> listarVentasPorTipo(TipoVenta tipoVenta) {
        return ventaRepository.findByTipoVenta(tipoVenta);
    }

    @Override
    public Optional<Venta> buscarVentaPorId(Long idVenta) {
        return ventaRepository.findById(idVenta);
    }
}
