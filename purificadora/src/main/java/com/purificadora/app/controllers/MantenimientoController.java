package com.purificadora.app.controllers;

import com.purificadora.app.models.Mantenimiento;
import com.purificadora.app.models.Producto;
import com.purificadora.app.repositories.ProductoRepository;
import com.purificadora.app.services.MantenimientoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/mantenimiento")
public class MantenimientoController {

    private final MantenimientoService mantenimientoService;
    private final ProductoRepository productoRepository;

    public MantenimientoController(MantenimientoService mantenimientoService, ProductoRepository productoRepository) {
        this.mantenimientoService = mantenimientoService;
        this.productoRepository = productoRepository;
    }

    @GetMapping
    public String lista(Model model) {
        List<Mantenimiento> lista = mantenimientoService.listarTodos();
        model.addAttribute("mantenimientos", lista);
        return "mantenimiento/lista-mantenimientos";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        Mantenimiento mant = new Mantenimiento();
        model.addAttribute("mantenimiento", mant);
        model.addAttribute("productos", productoRepository.findAll());
        return "mantenimiento/formulario-mantenimiento";
    }

    // Compatibilidad: redirigir rutas antiguas o enlaces directos
    @GetMapping("/formulario-mantenimiento")
    public String redirectOldForm() {
        return "redirect:/mantenimiento/nuevo";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Mantenimiento mantenimiento,
                          @RequestParam(value = "productoId", required = false) Long productoId,
                          @RequestParam(value = "fechaRealizadoStr", required = false) String fechaRealizadoStr,
                          RedirectAttributes ra) {

        // manejar producto seleccionado
        if (productoId != null) {
            productoRepository.findById(productoId).ifPresent(mantenimiento::setProducto);
        } else {
            mantenimiento.setProducto(null);
        }

        // parse fecha realizada si se envió
        if (fechaRealizadoStr != null && !fechaRealizadoStr.isBlank()) {
            try {
                // esperar formato yyyy-MM-dd'T'HH:mm (datetime-local)
                LocalDateTime parsed = LocalDateTime.parse(fechaRealizadoStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                mantenimiento.setFechaRealizado(parsed);
            } catch (Exception e) {
                // ignorar y dejar que el service ponga ahora
            }
        }

        // si proximaFecha está vacía, dejarla nula

        mantenimientoService.guardar(mantenimiento);
        ra.addFlashAttribute("success", "Mantenimiento registrado correctamente.");
        return "redirect:/mantenimiento";
    }
}
