
package com.example.api_franquicias.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.example.api_franquicias.dto.request.SucursalRequest;
import com.example.api_franquicias.dto.response.SucursalResponse;
import com.example.api_franquicias.service.SucursalService;

import jakarta.validation.Valid;

import com.example.api_franquicias.dto.request.NombreRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Sucursales",
        description = "Operaciones para administrar sucursales de franquicias"
)

@RestController
@RequestMapping
public class SucursalController {

    private final SucursalService sucursalService;

    public SucursalController(SucursalService sucursalService) {
        this.sucursalService = sucursalService;
    }

    @Operation(
            summary = "Crear sucursal",
            description = "Agrega una nueva sucursal a una franquicia existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sucursal creada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Franquicia no encontrada")
    })

    // Agregar una sucursal a una franquicia
    @PostMapping("/franquicias/{franquiciaId}/sucursales")
    public ResponseEntity<SucursalResponse> crear(
            @PathVariable Long franquiciaId,
            @Valid @RequestBody SucursalRequest request) {

        SucursalResponse respuesta =
                sucursalService.crear(franquiciaId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }


    @Operation(
            summary = "Listar sucursales",
            description = "Obtiene las sucursales pertenecientes a una franquicia"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta exitosa"),
            @ApiResponse(responseCode = "404", description = "Franquicia no encontrada")
    })
    // Listar las sucursales de una franquicia
    @GetMapping("/franquicias/{franquiciaId}/sucursales")
    public ResponseEntity<List<SucursalResponse>> listarPorFranquicia(
            @PathVariable Long franquiciaId) {

        return ResponseEntity.ok(
                sucursalService.listarPorFranquicia(franquiciaId)
        );
    }

    @Operation(
            summary = "Buscar sucursal",
            description = "Obtiene una sucursal utilizando su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucursal encontrada"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })

    // Buscar una sucursal por ID
    @GetMapping("/sucursales/{id}")
    public ResponseEntity<SucursalResponse> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(sucursalService.buscarPorId(id));
    }



    @Operation(
            summary = "Renombrar sucursal",
            description = "Actualiza el nombre de una sucursal existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Nombre actualizado"),
            @ApiResponse(responseCode = "400", description = "Nombre inválido"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })

    // Actualizar el nombre de una sucursal
    @PatchMapping("/sucursales/{id}/nombre")
    public ResponseEntity<SucursalResponse> renombrar(
            @PathVariable Long id,
            @Valid @RequestBody NombreRequest request) {

        return ResponseEntity.ok(
                sucursalService.renombrar(id, request)
        );
    }

}
