package apiblanck.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import apiblanck.model.Favorito;

/*
 * Implementación en memoria de FavoritoRepository.
 * Actualmente no se utiliza como adaptador principal de Spring.
 */
public class FavoritoRepositoryMemoria implements FavoritoRepository {

    private final Map<Long, Favorito> datos = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong();

    @Override
    public List<Favorito> buscarTodos() {
        return new ArrayList<>(datos.values());
    }

    @Override
    public Optional<Favorito> buscarPorId(Long id) {
        return Optional.ofNullable(datos.get(id));
    }

    @Override
    public Favorito guardar(Favorito favorito) {
        Long id = favorito.id() == null
            ? secuencia.incrementAndGet()
            : favorito.id();

        Favorito guardado = new Favorito(
            id,
            favorito.productoId(),
            favorito.nota(),
            favorito.fecha(),
            favorito.listaId()
        );

        datos.put(id, guardado);
        return guardado;
    }

    @Override
    public void eliminar(Long id) {
        datos.remove(id);
    }

    @Override
    public List<Favorito> buscarPorListaId(Long listaId) {
        return datos.values().stream()
            .filter(favorito -> favorito.listaId().equals(listaId))
            .toList();
    }
}