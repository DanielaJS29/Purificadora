package com.purificadora.app.services;

import com.purificadora.app.models.Categoria;
import com.purificadora.app.models.Producto;
import com.purificadora.app.repositories.CategoriaRepository;
import com.purificadora.app.repositories.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventarioServiceImpl implements InventarioService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public InventarioServiceImpl(CategoriaRepository categoriaRepository, ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public Optional<Categoria> buscarCategoriaPorId(Long idCategoria) {
        return categoriaRepository.findById(idCategoria);
    }

    @Override
    public Categoria guardarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public void eliminarCategoria(Long idCategoria) {
        categoriaRepository.deleteById(idCategoria);
    }

    @Override
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @Override
    public List<Producto> listarProductosPorCategoria(Long categoriaId) {
        if (categoriaId == null) {
            return listarProductos();
        }
        return productoRepository.findByCategoriaIdCategoria(categoriaId);
    }

    @Override
    public Optional<Producto> buscarProductoPorId(Long idProducto) {
        return productoRepository.findById(idProducto);
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public void eliminarProducto(Long idProducto) {
        productoRepository.deleteById(idProducto);
    }

    @Override
    public List<Producto> listarProductosEnStockCritico() {
        return productoRepository.findProductosEnStockCritico();
    }
}
