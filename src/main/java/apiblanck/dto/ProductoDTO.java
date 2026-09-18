package apiblanck.dto;
import java.math.BigDecimal;

public record ProductoDTO (
    Long id,
    String nombre,
    String descripcion,
    BigDecimal precio,
    String categoria

) {}
