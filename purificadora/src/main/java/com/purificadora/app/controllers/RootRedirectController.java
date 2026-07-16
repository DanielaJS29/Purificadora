package com.purificadora.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootRedirectController {

    @GetMapping("/formulario-producto.html")
    public String redirectFormularioProducto() {
        return "redirect:/inventario/productos/nuevo";
    }

    @GetMapping("/lista-productos.html")
    public String redirectListaProductos() {
        return "redirect:/inventario/productos";
    }

    @GetMapping("/inventario/productos.html")
    public String redirectInventarioProductosHtml() {
        return "redirect:/inventario/productos";
    }

    @GetMapping("/lista-categorias.html")
    public String redirectListaCategorias() {
        return "redirect:/inventario/categorias";
    }

    @GetMapping("/inventario/lista-categorias.html")
    public String redirectInventarioListaCategoriasHtml() {
        return "redirect:/inventario/categorias";
    }

    @GetMapping("/nueva-venta")
    public String redirectNuevaVenta() {
        return "redirect:/ventas/nueva";
    }
}
