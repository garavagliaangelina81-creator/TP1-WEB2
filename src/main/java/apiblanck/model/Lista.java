package apiblanck.model;
// dominio: LISTA con dueño 
public record Lista ( 
    Long id,
    String nombre,
    Long usuarioId // dueño de la lista
) {}
//LISTAS
//esta representa una lista en el dominio, igual que Favorito.