
package com.example.api_franquicias.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SucursalRequest(

        @NotBlank(message = "El nombre de la sucursal es obligatorio")
        String nombre

) {
}
