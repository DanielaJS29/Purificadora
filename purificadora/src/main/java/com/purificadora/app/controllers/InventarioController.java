package com.purificadora.app.controllers;

import com.purificadora.app.models.Categoria;
import com.purificadora.app.models.Producto;
import com.purificadora.app.services.InventarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping("/productos")
    public String listarProductos(@RequestParam(required = false) Long categoriaId, Model model) {
        List<Producto> productos = inventarioService.listarProductosPorCategoria(categoriaId);
        List<Categoria> categorias = inventarioService.listarCategorias();
        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categorias);
        model.addAttribute("categoriaId", categoriaId);
        return "inventario/lista-productos";
    }

    @GetMapping("/productos/criticos")
    public String listarProductosCriticos(Model model) {
        List<Producto> productosCriticos = inventarioService.listarProductosEnStockCritico();
        model.addAttribute("productosCriticos", productosCriticos);
        return "inventario/lista-productos";
    }

    @GetMapping("/productos/nuevo")
    public String mostrarFormularioProducto(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", inventarioService.listarCategorias());
        return "inventario/formulario-producto";
    }

    @GetMapping("/formulario-productos")
    public String mostrarFormularioProductoAlias(Model model) {
        return mostrarFormularioProducto(model);
    }

    @GetMapping("/productos/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return inventarioService.buscarProductoPorId(id)
                .map(producto -> {
                    model.addAttribute("producto", producto);
                    model.addAttribute("categorias", inventarioService.listarCategorias());
                    return "inventario/formulario-producto";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Producto no encontrado");
                    return "redirect:/inventario/productos";
                });
    }

    @PostMapping("/productos")
    public String guardarProducto(@Valid @ModelAttribute("producto") Producto producto,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categorias", inventarioService.listarCategorias());
            return "inventario/formulario-producto";
        }

        inventarioService.guardarProducto(producto);
        redirectAttributes.addFlashAttribute("success", "Producto guardado correctamente");
        return "redirect:/inventario/productos";
    }

    @PostMapping("/productos/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        inventarioService.eliminarProducto(id);
        redirectAttributes.addFlashAttribute("success", "Producto eliminado correctamente");
        return "redirect:/inventario/productos";
    }

    @GetMapping("/categorias")
    public String listarCategorias(Model model) {
        model.addAttribute("categoria", new Categoria());
        model.addAttribute("categorias", inventarioService.listarCategorias());
        return "inventario/lista-categorias";
    }

    @PostMapping("/categorias")
    public String guardarCategoria(@Valid @ModelAttribute("categoria") Categoria categoria,
                                   BindingResult bindingResult,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categorias", inventarioService.listarCategorias());
            return "inventario/lista-categorias";
        }

        inventarioService.guardarCategoria(categoria);
        redirectAttributes.addFlashAttribute("success", "Categoría guardada correctamente");
        return "redirect:/inventario/categorias";
    }

    @PostMapping("/categorias/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        inventarioService.eliminarCategoria(id);
        redirectAttributes.addFlashAttribute("success", "Categoría eliminada correctamente");
        return "redirect:/inventario/categorias";
    }
}
