package com.purificadora.app.services;

import com.purificadora.app.models.Cliente;
import com.purificadora.app.models.DetalleVenta;
import com.purificadora.app.models.TipoVenta;
import com.purificadora.app.models.Venta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VentaService {

    List<Cliente> listarClientes();

    Optional<Cliente> buscarClientePorId(Long idCliente);

    Cliente guardarCliente(Cliente cliente);

    void eliminarCliente(Long idCliente);

    Venta registrarVenta(Venta venta, List<DetalleVenta> detalles);

    List<Venta> listarVentas();

    List<Venta> listarVentasPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<Venta> listarVentasPorTipo(TipoVenta tipoVenta);

    Optional<Venta> buscarVentaPorId(Long idVenta);
}
