
package com.example.api_franquicias.dto.request;

import jakarta.validation.constraints.NotBlank;

public record NombreRequest(

        @NotBlank(message = "El nombre es obligatorio")
        String nombre

) {
}
