
package com.example.api_franquicias.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Datos necesarios para crear un producto")
public record ProductoRequest(

        @Schema(
                description = "Nombre del producto",
                example = "Empanada de pollo"
        )
        @NotBlank(message = "El nombre del producto es obligatorio")
        String nombre,

        @Schema(
                description = "Cantidad disponible en inventario",
                example = "150",
                minimum = "0"
        )
        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock

) {
}
