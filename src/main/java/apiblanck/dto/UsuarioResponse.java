package apiblanck.dto;

import apiblanck.model.Rol;

public record UsuarioResponse(
    Long id,
    String email,
    Rol rol
) {} 
