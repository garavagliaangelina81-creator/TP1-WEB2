package apiblanck.dto;

import java.time.LocalDate;

public record FavoritoResponse (
    Long id,
    Long productoId,
    String nota,
    LocalDate fecha
) {}

/**
 * este dto es para una respuesta de favorito, para enviar la informacion de un favorito 
 */