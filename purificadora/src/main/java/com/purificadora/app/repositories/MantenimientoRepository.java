package com.purificadora.app.repositories;

import com.purificadora.app.models.Mantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Long> {

    List<Mantenimiento> findByProximaFechaLessThanEqual(LocalDate fecha);
}
