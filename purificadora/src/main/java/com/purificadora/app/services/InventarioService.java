package com.purificadora.app.services;

import com.purificadora.app.models.Categoria;
import com.purificadora.app.models.Producto;

import java.util.List;
import java.util.Optional;

public interface InventarioService {

    List<Categoria> listarCategorias();

    Optional<Categoria> buscarCategoriaPorId(Long idCategoria);

    Categoria guardarCategoria(Categoria categoria);

    void eliminarCategoria(Long idCategoria);

    List<Producto> listarProductos();

    List<Producto> listarProductosPorCategoria(Long categoriaId);

    Optional<Producto> buscarProductoPorId(Long idProducto);

    Producto guardarProducto(Producto producto);

    void eliminarProducto(Long idProducto);

    List<Producto> listarProductosEnStockCritico();
}
