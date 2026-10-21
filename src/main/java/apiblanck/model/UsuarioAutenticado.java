package apiblanck.model;

public record UsuarioAutenticado(
    Long id,
    String email,
    Rol rol
) {
    public boolean esAdmin() {
        return rol == Rol.ADMIN;
    }
}