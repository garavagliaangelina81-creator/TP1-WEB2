package apiblanck.repository;

import apiblanck.model.Favorito;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
@Primary
@Repository
public class FavoritoRepositoryImpl implements FavoritoRepository {

    private final Map<Long, Favorito> favoritos = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<Favorito> buscarTodos() {
        return List.copyOf(favoritos.values());
    }

    @Override
    public Optional<Favorito> buscarPorId(Long id) {
        return Optional.ofNullable(favoritos.get(id));
    }

    @Override
    public Favorito guardar(Favorito favorito) {
        if (favorito.id() == null) {
            Long nuevoId = idGenerator.getAndIncrement();
            Favorito nuevoFavorito = new Favorito(
                nuevoId,
                favorito.productoId(),
                favorito.nota(),
                favorito.fecha()
            );
            favoritos.put(nuevoId, nuevoFavorito);
            return nuevoFavorito;
        } else {
            favoritos.put(favorito.id(), favorito);
            return favorito;
        }
    }

    @Override
    public void eliminar(Long id) {
        favoritos.remove(id);
    }
}