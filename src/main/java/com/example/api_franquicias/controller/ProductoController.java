
package com.example.api_franquicias.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import com.example.api_franquicias.dto.request.ProductoRequest;
import com.example.api_franquicias.dto.request.StockRequest;
import com.example.api_franquicias.dto.response.ProductoResponse;
import com.example.api_franquicias.service.ProductoService;

import jakarta.validation.Valid;

import com.example.api_franquicias.dto.request.NombreRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(
        name = "Productos",
        description = "Operaciones para administrar productos y su stock"
)

@RestController
@RequestMapping
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }


    @Operation(
            summary = "Crear producto",
            description = "Agrega un producto a una sucursal existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })

    // Agregar un producto a una sucursal
    @PostMapping("/sucursales/{sucursalId}/productos")
    public ResponseEntity<ProductoResponse> crear(
            @PathVariable Long sucursalId,
            @Valid @RequestBody ProductoRequest request) {

        ProductoResponse respuesta =
                productoService.crear(sucursalId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }

    @Operation(
            summary = "Listar productos",
            description = "Obtiene todos los productos de una sucursal"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta exitosa"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })

    // Listar productos de una sucursal
    @GetMapping("/sucursales/{sucursalId}/productos")
    public ResponseEntity<List<ProductoResponse>> listarPorSucursal(
            @PathVariable Long sucursalId) {

        return ResponseEntity.ok(
                productoService.listarPorSucursal(sucursalId)
        );
    }


    @Operation(
            summary = "Buscar producto",
            description = "Obtiene un producto utilizando su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    // Buscar producto por ID
    @GetMapping("/productos/{id}")
    public ResponseEntity<ProductoResponse> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(productoService.buscarPorId(id));
    }



    @Operation(
            summary = "Actualizar stock",
            description = "Modifica la cantidad de stock de un producto existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock actualizado"),
            @ApiResponse(responseCode = "400", description = "Stock inválido"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    // Actualizar stock de un producto
    @PutMapping("/productos/{id}/stock")
    public ResponseEntity<ProductoResponse> actualizarStock(
            @PathVariable Long id,
            @Valid @RequestBody StockRequest request) {

        return ResponseEntity.ok(
                productoService.actualizarStock(id, request)
        );
    }


    @Operation(
            summary = "Eliminar producto",
            description = "Elimina un producto de la base de datos"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })

    // Eliminar un producto
    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        productoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }



    @Operation(
            summary = "Renombrar producto",
            description = "Actualiza el nombre de un producto existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Nombre actualizado"),
            @ApiResponse(responseCode = "400", description = "Nombre inválido"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })

    // Actualizar el nombre de un producto
    @PatchMapping("/productos/{id}/nombre")
    public ResponseEntity<ProductoResponse> renombrar(
            @PathVariable Long id,
            @Valid @RequestBody NombreRequest request) {

        return ResponseEntity.ok(
                productoService.renombrar(id, request)
        );
    }

}
