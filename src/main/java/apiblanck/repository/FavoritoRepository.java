package apiblanck.repository;

import java.util.List;
import java.util.Optional;
import apiblanck.model.Favorito;

/*Es la interfaz que deffine el contrato.
//Es el puerto, la abstracción que define lo que se puede hacer con los productos.
*/
public interface FavoritoRepository { 
    List<Favorito> buscarTodos();
    Optional<Favorito> buscarPorId(Long id);
    Favorito guardar(Favorito favorito);
    void eliminar(Long id);
}