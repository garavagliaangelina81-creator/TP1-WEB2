package apiblanck.model;
import java.time.LocalDate;

public record Favorito ( 
    Long id,
    Long productoId,
    String nota,
    LocalDate fecha
    
 ) {}
//este es un molde de datos que representa un producto en la app
