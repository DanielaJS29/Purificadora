package com.purificadora.app.controllers;

import com.purificadora.app.models.Cliente;
import com.purificadora.app.models.DetalleVenta;
import com.purificadora.app.models.Producto;
import com.purificadora.app.models.TipoVenta;
import com.purificadora.app.models.Venta;
import com.purificadora.app.services.InventarioService;
import com.purificadora.app.services.UsuarioService;
import com.purificadora.app.services.VentaService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;
    private final InventarioService inventarioService;
    private final UsuarioService usuarioService;

    public VentaController(VentaService ventaService, InventarioService inventarioService, UsuarioService usuarioService) {
        this.ventaService = ventaService;
        this.inventarioService = inventarioService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/clientes")
    public String listarClientes(Model model) {
        model.addAttribute("clientes", ventaService.listarClientes());
        model.addAttribute("cliente", new Cliente());
        return "ventas/lista-clientes";
    }

    @PostMapping("/clientes")
    public String guardarCliente(@Valid @ModelAttribute("cliente") Cliente cliente,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("clientes", ventaService.listarClientes());
            return "ventas/lista-clientes";
        }
        ventaService.guardarCliente(cliente);
        redirectAttributes.addFlashAttribute("success", "Cliente guardado correctamente");
        return "redirect:/ventas/clientes";
    }

    @GetMapping("/nueva")
    public String nuevaVenta(Model model, Authentication authentication) {
        Venta venta = new Venta();
        Optional<Cliente> publicoGeneral = ventaService.listarClientes().stream()
                .filter(c -> "Público General".equalsIgnoreCase(c.getNombre()))
                .findFirst();
        if (publicoGeneral.isPresent()) {
            venta.setCliente(publicoGeneral.get());
        }
        model.addAttribute("venta", venta);
        model.addAttribute("clientes", ventaService.listarClientes());
        model.addAttribute("productos", inventarioService.listarProductos());
        model.addAttribute("tiposVenta", TipoVenta.values());
        model.addAttribute("metodosPago", com.purificadora.app.models.MetodoPago.values());
        model.addAttribute("usuario", getAuthenticatedUser(authentication));
        return "ventas/nueva-venta";
    }

    @PostMapping("/guardar")
    public String guardarVenta(@ModelAttribute Venta venta,
                               @RequestParam(value = "clienteId", required = false) Long clienteId,
                               @RequestParam(value = "productoId", required = false) List<Long> productoIds,
                               @RequestParam(value = "cantidad", required = false) List<Integer> cantidades,
                               @RequestParam(value = "precioUnitario", required = false) List<BigDecimal> precios,
                               @RequestParam(value = "subtotal", required = false) List<BigDecimal> subtotales,
                               RedirectAttributes redirectAttributes,
                               Authentication authentication) {

        if (clienteId != null && clienteId > 0) {
            ventaService.buscarClientePorId(clienteId).ifPresent(venta::setCliente);
        } else {
            venta.setCliente(null);
        }
        venta.setUsuario(getAuthenticatedUser(authentication));

        List<DetalleVenta> detalles = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        if (productoIds != null) {
            for (int i = 0; i < productoIds.size(); i++) {
                DetalleVenta detalle = new DetalleVenta();
                detalle.setProducto(inventarioService.buscarProductoPorId(productoIds.get(i)).orElseThrow());
                detalle.setCantidad(cantidades.get(i));
                detalle.setPrecioUnitario(precios.get(i));
                detalle.setSubtotal(subtotales.get(i));
                total = total.add(subtotales.get(i));
                detalles.add(detalle);
            }
        }
        venta.setTotal(total);
        ventaService.registrarVenta(venta, detalles);
        redirectAttributes.addFlashAttribute("success", "Venta registrada correctamente");
        return "redirect:/ventas/historial";
    }

    @GetMapping("/historial")
    public String historialVentas(@RequestParam(required = false) TipoVenta tipoVenta,
                                  @RequestParam(required = false) String desde,
                                  @RequestParam(required = false) String hasta,
                                  Model model) {
        List<com.purificadora.app.models.Venta> ventas;
        if (desde != null && hasta != null) {
            LocalDate fechaInicio = LocalDate.parse(desde);
            LocalDate fechaFin = LocalDate.parse(hasta);
            ventas = ventaService.listarVentasPorFecha(fechaInicio.atStartOfDay(), fechaFin.atTime(LocalTime.MAX));
        } else if (tipoVenta != null) {
            ventas = ventaService.listarVentasPorTipo(tipoVenta);
        } else {
            ventas = ventaService.listarVentas();
        }
        model.addAttribute("ventas", ventas);
        model.addAttribute("tiposVenta", TipoVenta.values());
        return "ventas/historial-ventas";
    }

    @GetMapping("/detalle/{id}")
    public String detalleVenta(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return ventaService.buscarVentaPorId(id)
                .map(venta -> {
                    model.addAttribute("venta", venta);
                    return "ventas/detalle-venta";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Venta no encontrada");
                    return "redirect:/ventas/historial";
                });
    }

    private com.purificadora.app.entities.Usuario getAuthenticatedUser(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return usuarioService.buscarPorCorreo(userDetails.getUsername()).orElse(null);
        }
        return null;
    }
}
