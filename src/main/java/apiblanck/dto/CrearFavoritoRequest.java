package apiblanck.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Este DTO es para solicitar la creación de un favorito,
// es decir, para recibir la información desde el cliente.

public record CrearFavoritoRequest(

    @NotNull
    Long productoId,

    @NotBlank
    @Size(max = 200)
    String nota,

    @NotNull
    Long listaId

) {}

/* si alguien intenta crear un favorito sin indicar la lista, la validacion del request lo rechaza */