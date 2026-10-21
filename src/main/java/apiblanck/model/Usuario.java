package apiblanck.model;

public record Usuario ( 
    Long id,
    String email,
    String passwordHash, //no va a guardarse contraseña en texto plano.
    Rol rol
    
) {}
