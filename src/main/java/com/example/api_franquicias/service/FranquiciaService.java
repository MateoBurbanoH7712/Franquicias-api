
package com.example.api_franquicias.service;

import com.example.api_franquicias.dto.request.FranquiciaRequest;
import com.example.api_franquicias.dto.request.NombreRequest;
import com.example.api_franquicias.dto.response.FranquiciaResponse;
import com.example.api_franquicias.dto.response.ProductoMayorStockResponse;
import com.example.api_franquicias.entity.Franquicia;
import com.example.api_franquicias.entity.Producto;
import com.example.api_franquicias.entity.Sucursal;
import com.example.api_franquicias.repository.FranquiciaRepository;
import com.example.api_franquicias.repository.ProductoRepository;
import com.example.api_franquicias.repository.SucursalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class FranquiciaService {

    private final FranquiciaRepository franquiciaRepository;
    private final SucursalRepository sucursalRepository;
    private final ProductoRepository productoRepository;

    public FranquiciaService(
            FranquiciaRepository franquiciaRepository,
            SucursalRepository sucursalRepository,
            ProductoRepository productoRepository) {
        this.franquiciaRepository = franquiciaRepository;
        this.sucursalRepository = sucursalRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    public FranquiciaResponse crear(FranquiciaRequest request) {
        Franquicia franquicia = new Franquicia(request.nombre());
        Franquicia guardada = franquiciaRepository.save(franquicia);
        return convertirRespuesta(guardada);
    }

    @Transactional(readOnly = true)
    public List<FranquiciaResponse> listar() {
        return franquiciaRepository.findAll()
                .stream()
                .map(this::convertirRespuesta)
                .toList();
    }

    @Transactional(readOnly = true)
    public FranquiciaResponse buscarPorId(Long id) {
        Franquicia franquicia = obtenerEntidad(id);
        return convertirRespuesta(franquicia);
    }

    @Transactional
    public FranquiciaResponse renombrar(Long id, NombreRequest request) {
        Franquicia franquicia = obtenerEntidad(id);
        franquicia.setNombre(request.nombre());
        return convertirRespuesta(franquicia);
    }


    @Transactional(readOnly = true)
    public List<ProductoMayorStockResponse> productosMayorStock(Long id) {

        // Verificar que la franquicia exista
        obtenerEntidad(id);

        // Obtener las sucursales de la franquicia
        List<Sucursal> sucursales =
                sucursalRepository.findByFranquiciaId(id);

        // Buscar el producto con mayor stock por sucursal
        return sucursales.stream()
                .flatMap(sucursal ->
                        productoRepository
                                .findFirstBySucursalIdOrderByStockDescIdAsc(
                                        sucursal.getId())
                                .stream()
                                .map(producto ->
                                        convertirMayorStock(sucursal, producto)))
                .toList();
    }


    private Franquicia obtenerEntidad(Long id) {
        return franquiciaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Franquicia no encontrada: " + id));
    }

    private FranquiciaResponse convertirRespuesta(Franquicia franquicia) {
        return new FranquiciaResponse(
                franquicia.getId(),
                franquicia.getNombre()
        );
    }

    private ProductoMayorStockResponse convertirMayorStock(
            Sucursal sucursal, Producto producto) {
        return new ProductoMayorStockResponse(
                sucursal.getId(),
                sucursal.getNombre(),
                producto.getId(),
                producto.getNombre(),
                producto.getStock()
        );
    }
}
