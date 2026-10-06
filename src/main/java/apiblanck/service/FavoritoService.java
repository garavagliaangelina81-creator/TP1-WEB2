package apiblanck.service;

import apiblanck.dto.CrearFavoritoRequest;
import apiblanck.dto.FavoritoResponse;
import apiblanck.repository.FavoritoRepository;
import apiblanck.model.Favorito;
import apiblanck.exception.RecursoNoEncontradoException;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class FavoritoService {

    private final FavoritoRepository repository;

    public FavoritoService(FavoritoRepository repository) {
        this.repository = repository;
    }

    public List<FavoritoResponse> listar(String texto) {
        String filtro = texto == null ? "" : texto.trim().toLowerCase();

        return repository.buscarTodos().stream()
            .filter(favorito -> favorito.nota().toLowerCase().contains(filtro))
            .map(this::aResponse)
            .toList();
    }

    public FavoritoResponse buscar(Long id) {
        return repository.buscarPorId(id)
            .map(this::aResponse)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "No existe el Favorito " + id
            ));
    }

    public FavoritoResponse crear(CrearFavoritoRequest request) {
        Favorito nuevo = new Favorito(
            null,
            request.productoId(),
            request.nota(),
            java.time.LocalDate.now(),
            request.listaId()
        );

        return aResponse(repository.guardar(nuevo));
    }

    public FavoritoResponse actualizar(
        Long id,
        CrearFavoritoRequest request
    ) {
        buscar(id);

        Favorito actualizado = new Favorito(
            id,
            request.productoId(),
            request.nota(),
            java.time.LocalDate.now(),
            request.listaId()
        );

        return aResponse(repository.guardar(actualizado));
    }

    public void eliminar(Long id) {
        buscar(id);
        repository.eliminar(id);
    }

    private FavoritoResponse aResponse(Favorito favorito) {
        return new FavoritoResponse(
            favorito.id(),
            favorito.productoId(),
            favorito.nota(),
            favorito.fecha(),
            favorito.listaId()
        );
    }
}

/* ahora el servicio toma el listaId del request, cuando crea o actualiza
y tmb lo devuelbe en la respuesta favorito.listaId()  */
/* ahora los dos repo viejos que conservamos pero ya no usan Spring como implementacion principal. */