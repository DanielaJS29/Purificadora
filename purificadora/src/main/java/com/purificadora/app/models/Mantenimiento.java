package com.purificadora.app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "mantenimientos")
public class Mantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMantenimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @NotBlank
    @Column(name = "tipo_mantenimiento", nullable = false, length = 150)
    private String tipoMantenimiento;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_realizado")
    private LocalDateTime fechaRealizado;

    @Column(name = "proxima_fecha")
    private LocalDate proximaFecha;

    public Mantenimiento() {
    }

    public Long getIdMantenimiento() {
        return idMantenimiento;
    }

    public void setIdMantenimiento(Long idMantenimiento) {
        this.idMantenimiento = idMantenimiento;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getTipoMantenimiento() {
        return tipoMantenimiento;
    }

    public void setTipoMantenimiento(String tipoMantenimiento) {
        this.tipoMantenimiento = tipoMantenimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaRealizado() {
        return fechaRealizado;
    }

    public void setFechaRealizado(LocalDateTime fechaRealizado) {
        this.fechaRealizado = fechaRealizado;
    }

    public LocalDate getProximaFecha() {
        return proximaFecha;
    }

    public void setProximaFecha(LocalDate proximaFecha) {
        this.proximaFecha = proximaFecha;
    }
}
