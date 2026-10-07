package apiblanck.dto;

import jakarta.validation.constraints.NotNull;

public record MoverFavoritosRequest(
    @NotNull Long destinoId
) {
}
//mover favoritos de una lista a otra de una transaccion.
// esta representa el JSON  que vamos a enviar.