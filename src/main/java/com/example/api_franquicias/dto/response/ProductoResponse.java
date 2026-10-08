
package com.example.api_franquicias.dto.response;

public record ProductoResponse(

        Long id,
        String nombre,
        Integer stock,
        Long sucursalId

) {
}
