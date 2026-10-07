package apiblanck.service;

import apiblanck.model.Favorito;
import apiblanck.model.Lista;
import apiblanck.repository.FavoritoRepository;
import apiblanck.repository.ListaRepository;
import apiblanck.exception.RecursoEnConflictoException;
import apiblanck.exception.RecursoNoEncontradoException;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ListaService {

    private final ListaRepository listaRepository;
    private final FavoritoRepository favoritoRepository;

    public ListaService(
        ListaRepository listaRepository,
        FavoritoRepository favoritoRepository
    ) {
        this.listaRepository = listaRepository;
        this.favoritoRepository = favoritoRepository;
    }

    public List<Lista> listar() {
        return listaRepository.buscarTodos();
    }

    public Lista buscar(Long id) {
        return listaRepository.buscarPorId(id)
            .orElseThrow(() ->
                new RuntimeException("No existe la Lista " + id)
            );
    }

    public Lista crear(String nombre) {
        Lista nueva = new Lista(
            null,
            nombre
        );

        return listaRepository.guardar(nueva);
    }
    public void eliminar(Long id) {
    buscar(id);

    List<Favorito> favoritos = favoritoRepository.buscarPorListaId(id);

    if (!favoritos.isEmpty()) {
        throw new RecursoEnConflictoException(
            "No se puede eliminar la lista porque contiene favoritos"
        );
    }

    listaRepository.eliminar(id);
}

    public List<Favorito> listarFavoritos(Long listaId) {
        buscar(listaId);

        return favoritoRepository.buscarPorListaId(listaId);
    }
    @Transactional
public void moverFavoritos(Long origenId, Long destinoId) {
    Lista origen = listaRepository.buscarPorId(origenId)
        .orElseThrow(() ->
            new RecursoNoEncontradoException(
                "No existe la lista de origen " + origenId
            )
        );

    listaRepository.buscarPorId(destinoId)
        .orElseThrow(() ->
            new RecursoNoEncontradoException(
                "No existe la lista de destino " + destinoId
            )
        );

    List<Favorito> favoritos = favoritoRepository.buscarPorListaId(origenId);

    for (Favorito favorito : favoritos) {
        Favorito movido = new Favorito(
            favorito.id(),
            favorito.productoId(),
            favorito.nota(),
            favorito.fecha(),
            destinoId
        );

        favoritoRepository.guardar(movido);
    }

    listaRepository.eliminar(origen.id());
}
}
/* por ahora hacemos las operaciones basicas, listar, buscar y crearq  */
/* get/ listas - listar()
get /listas/{id} - buscar
post /listas - crear() */
/* listaId
   ↓
verifica que la lista exista
   ↓
busca sus favoritos
   ↓
devuelve los favoritos */
/*Verifica que exista la lista origen → 404 si no existe.
Verifica que exista la lista destino → 404 si no existe.
Mueve todos los favoritos.
Elimina la lista origen.
@Transactional hace que toda la operación sea atómica: si algo falla, se revierte la operación. */
