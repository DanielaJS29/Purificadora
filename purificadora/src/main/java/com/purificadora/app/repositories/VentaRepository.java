package com.purificadora.app.repositories;

import com.purificadora.app.models.TipoVenta;
import com.purificadora.app.models.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByFechaVentaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<Venta> findByTipoVenta(TipoVenta tipoVenta);
}
