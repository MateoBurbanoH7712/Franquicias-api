
package com.example.api_franquicias.service;

import com.example.api_franquicias.dto.request.NombreRequest;
import com.example.api_franquicias.dto.request.SucursalRequest;
import com.example.api_franquicias.dto.response.SucursalResponse;
import com.example.api_franquicias.entity.Franquicia;
import com.example.api_franquicias.entity.Sucursal;
import com.example.api_franquicias.repository.FranquiciaRepository;
import com.example.api_franquicias.repository.SucursalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class SucursalService {

    private final SucursalRepository sucursalRepository;
    private final FranquiciaRepository franquiciaRepository;

    public SucursalService(
            SucursalRepository sucursalRepository,
            FranquiciaRepository franquiciaRepository) {
        this.sucursalRepository = sucursalRepository;
        this.franquiciaRepository = franquiciaRepository;
    }

    @Transactional
    public SucursalResponse crear(Long franquiciaId, SucursalRequest request) {
        Franquicia franquicia = franquiciaRepository
                .findById(franquiciaId)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Franquicia no encontrada: " + franquiciaId));

        Sucursal sucursal = new Sucursal(request.nombre(), franquicia);
        Sucursal guardada = sucursalRepository.save(sucursal);

        return convertirRespuesta(guardada);
    }

    @Transactional(readOnly = true)
    public List<SucursalResponse> listarPorFranquicia(Long franquiciaId) {
        if (!franquiciaRepository.existsById(franquiciaId)) {
            throw new ResponseStatusException(
                    NOT_FOUND, "Franquicia no encontrada: " + franquiciaId);
        }

        return sucursalRepository.findByFranquiciaId(franquiciaId)
                .stream()
                .map(this::convertirRespuesta)
                .toList();
    }

    @Transactional(readOnly = true)
    public SucursalResponse buscarPorId(Long id) {
        return convertirRespuesta(obtenerEntidad(id));
    }

    @Transactional
    public SucursalResponse renombrar(Long id, NombreRequest request) {
        Sucursal sucursal = obtenerEntidad(id);
        sucursal.setNombre(request.nombre());
        return convertirRespuesta(sucursal);
    }

    private Sucursal obtenerEntidad(Long id) {
        return sucursalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Sucursal no encontrada: " + id));
    }

    private SucursalResponse convertirRespuesta(Sucursal sucursal) {
        return new SucursalResponse(
                sucursal.getId(),
                sucursal.getNombre(),
                sucursal.getFranquicia().getId()
        );
    }
}
