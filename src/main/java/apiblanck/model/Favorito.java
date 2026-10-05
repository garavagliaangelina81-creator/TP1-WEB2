package apiblanck.model;
import java.time.LocalDate;

public record Favorito ( 
    Long id,
    Long productoId,
    String nota,
    LocalDate fecha,
    Long listaId //esto nos permite que el dominio maneje el ident de la lista sin depender de JPA ni de listaEntity.
    
 ) {}
//este es un molde de datos que representa un producto en la app
