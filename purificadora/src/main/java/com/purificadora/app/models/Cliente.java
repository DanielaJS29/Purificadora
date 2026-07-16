package com.purificadora.app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Size(max = 15, message = "El teléfono no puede exceder 15 caracteres")
    @Column(length = 15)
    private String telefono;

    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    @Column(length = 255)
    private String direccion;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String referenciasDireccion;

    @Column(nullable = false)
    private Integer saldoEnvases = 0;

    @CreationTimestamp
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    public Cliente() {
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getReferenciasDireccion() {
        return referenciasDireccion;
    }

    public void setReferenciasDireccion(String referenciasDireccion) {
        this.referenciasDireccion = referenciasDireccion;
    }

    public Integer getSaldoEnvases() {
        return saldoEnvases;
    }

    public void setSaldoEnvases(Integer saldoEnvases) {
        this.saldoEnvases = saldoEnvases;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
