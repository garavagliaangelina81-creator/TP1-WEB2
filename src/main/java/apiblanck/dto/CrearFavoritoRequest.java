package apiblanck.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// este dto es para solicitar la creacion de un favorito, es decir, para recibir la informacion desde el client
public record CrearFavoritoRequest (
    @NotNull Long productoId, // id del producto en la api externa
    @NotBlank @Size(max = 200) String nota // nota personal del usuario sobre el producto
) {}
