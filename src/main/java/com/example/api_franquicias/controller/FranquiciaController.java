
package com.example.api_franquicias.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.example.api_franquicias.dto.request.FranquiciaRequest;
import com.example.api_franquicias.dto.response.FranquiciaResponse;
import com.example.api_franquicias.dto.response.ProductoMayorStockResponse;
import com.example.api_franquicias.service.FranquiciaService;

import com.example.api_franquicias.dto.request.NombreRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Franquicias",
        description = "Operaciones para administrar franquicias"
)
@RestController
@RequestMapping("/franquicias")
public class FranquiciaController {

    private final FranquiciaService franquiciaService;

    public FranquiciaController(FranquiciaService franquiciaService) {
        this.franquiciaService = franquiciaService;
    }


    @Operation(
            summary = "Crear franquicia",
            description = "Registra una nueva franquicia en la base de datos"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Franquicia creada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    // Crear una franquicia
    @PostMapping
    public ResponseEntity<FranquiciaResponse> crear(
            @Valid @RequestBody FranquiciaRequest request) {

        FranquiciaResponse respuesta = franquiciaService.crear(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }


    @Operation(
            summary = "Listar franquicias",
            description = "Obtiene todas las franquicias registradas"
    )
    @ApiResponse(responseCode = "200", description = "Consulta exitosa")

    // Listar todas las franquicias
    @GetMapping
    public ResponseEntity<List<FranquiciaResponse>> listar() {

        return ResponseEntity.ok(franquiciaService.listar());
    }



    @Operation(
            summary = "Buscar franquicia",
            description = "Consulta una franquicia utilizando su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Franquicia encontrada"),
            @ApiResponse(responseCode = "404", description = "Franquicia no encontrada")
    })
    // Buscar una franquicia por ID
    @GetMapping("/{id}")
    public ResponseEntity<FranquiciaResponse> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(franquiciaService.buscarPorId(id));
    }




    @Operation(
            summary = "Productos con mayor stock por sucursal",
            description = "Obtiene el producto con mayor stock de cada sucursal de una franquicia"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta exitosa"),
            @ApiResponse(responseCode = "404", description = "Franquicia no encontrada")
    })
    // Consultar producto con mayor stock por sucursal
    @GetMapping("/{id}/productos-mayor-stock")
    public ResponseEntity<List<ProductoMayorStockResponse>> productosMayorStock(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                franquiciaService.productosMayorStock(id)
        );
    }


    @Operation(
            summary = "Renombrar franquicia",
            description = "Actualiza el nombre de una franquicia existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Nombre actualizado"),
            @ApiResponse(responseCode = "400", description = "Nombre inválido"),
            @ApiResponse(responseCode = "404", description = "Franquicia no encontrada")
    })

    // Actualizar el nombre de una franquicia
    @PatchMapping("/{id}/nombre")
    public ResponseEntity<FranquiciaResponse> renombrar(
            @PathVariable Long id,
            @Valid @RequestBody NombreRequest request) {

        return ResponseEntity.ok(
                franquiciaService.renombrar(id, request)
        );
    }


}
