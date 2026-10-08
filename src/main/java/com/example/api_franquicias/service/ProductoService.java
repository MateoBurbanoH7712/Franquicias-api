
package com.example.api_franquicias.service;

import com.example.api_franquicias.dto.request.NombreRequest;
import com.example.api_franquicias.dto.request.ProductoRequest;
import com.example.api_franquicias.dto.request.StockRequest;
import com.example.api_franquicias.dto.response.ProductoResponse;
import com.example.api_franquicias.entity.Producto;
import com.example.api_franquicias.entity.Sucursal;
import com.example.api_franquicias.repository.ProductoRepository;
import com.example.api_franquicias.repository.SucursalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;

    public ProductoService(
            ProductoRepository productoRepository,
            SucursalRepository sucursalRepository) {
        this.productoRepository = productoRepository;
        this.sucursalRepository = sucursalRepository;
    }

    @Transactional
    public ProductoResponse crear(Long sucursalId, ProductoRequest request) {
        Sucursal sucursal = sucursalRepository.findById(sucursalId)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Sucursal no encontrada: " + sucursalId));

        Producto producto = new Producto(
                request.nombre(),
                request.stock(),
                sucursal
        );

        Producto guardado = productoRepository.save(producto);
        return convertirRespuesta(guardado);
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listarPorSucursal(Long sucursalId) {
        if (!sucursalRepository.existsById(sucursalId)) {
            throw new ResponseStatusException(
                    NOT_FOUND, "Sucursal no encontrada: " + sucursalId);
        }

        return productoRepository.findBySucursalId(sucursalId)
                .stream()
                .map(this::convertirRespuesta)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponse buscarPorId(Long id) {
        return convertirRespuesta(obtenerEntidad(id));
    }

    @Transactional
    public ProductoResponse actualizarStock(Long id, StockRequest request) {
        Producto producto = obtenerEntidad(id);
        producto.setStock(request.stock());
        return convertirRespuesta(producto);
    }

    @Transactional
    public ProductoResponse renombrar(Long id, NombreRequest request) {
        Producto producto = obtenerEntidad(id);
        producto.setNombre(request.nombre());
        return convertirRespuesta(producto);
    }

    @Transactional
    public void eliminar(Long id) {
        Producto producto = obtenerEntidad(id);
        productoRepository.delete(producto);
    }

    private Producto obtenerEntidad(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Producto no encontrado: " + id));
    }

    private ProductoResponse convertirRespuesta(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getStock(),
                producto.getSucursal().getId()
        );
    }
}
