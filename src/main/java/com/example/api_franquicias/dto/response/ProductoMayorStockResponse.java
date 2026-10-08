
package com.example.api_franquicias.dto.response;

public record ProductoMayorStockResponse(

        Long sucursalId,
        String sucursalNombre,
        Long productoId,
        String productoNombre,
        Integer stock

) {
}
