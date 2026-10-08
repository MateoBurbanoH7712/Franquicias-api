
package com.example.api_franquicias.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FranquiciaRequest(

        @NotBlank(message = "El nombre de la franquicia es obligatorio")
        String nombre

) {
}
