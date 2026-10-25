package apiblanck.service;

import apiblanck.dto.CrearFavoritoRequest;
import apiblanck.dto.FavoritoResponse;
import apiblanck.exception.RecursoNoEncontradoException;
import apiblanck.model.Favorito;
import apiblanck.model.Lista;
import apiblanck.model.UsuarioAutenticado;
import apiblanck.repository.FavoritoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoritoService {

    private final FavoritoRepository repository;
    private final ListaService listaService;

    public FavoritoService(FavoritoRepository repository, ListaService listaService) {
        this.repository = repository;
        this.listaService = listaService;
    }

    public List<FavoritoResponse> listar(String texto, UsuarioAutenticado usuario) {
        String filtro = texto == null ? "" : texto.trim().toLowerCase();

        // Un usuario común ve solo los favoritos de SUS listas; un ADMIN ve todos
        List<Favorito> visibles;
        if (usuario.esAdmin()) {
            visibles = repository.buscarTodos();
        } else {
            visibles = listaService.listar(usuario).stream()
                .flatMap(lista -> repository.buscarPorListaId(lista.id()).stream())
                .toList();
        }

        return visibles.stream()
            .filter(favorito -> favorito.nota().toLowerCase().contains(filtro))
            .map(this::aResponse)
            .toList();
    }

    public FavoritoResponse buscar(Long id, UsuarioAutenticado usuario) {
        return aResponse(obtenerConAcceso(id, usuario));
    }

    public FavoritoResponse crear(CrearFavoritoRequest request, UsuarioAutenticado usuario) {
        // 404 si la lista no existe, 403 si no es suya
        Lista lista = listaService.buscar(request.listaId(), usuario);

        Favorito nuevo = new Favorito(
            null,
            request.productoId(),
            request.nota(),
            java.time.LocalDate.now(),
            lista.id()
        );

        return aResponse(repository.guardar(nuevo));
    }

    public FavoritoResponse actualizar(
        Long id,
        CrearFavoritoRequest request,
        UsuarioAutenticado usuario
    ) {
        obtenerConAcceso(id, usuario);
        // Si lo mueve a otra lista, también tiene que ser dueño de la lista destino
        Lista destino = listaService.buscar(request.listaId(), usuario);

        Favorito actualizado = new Favorito(
            id,
            request.productoId(),
            request.nota(),
            java.time.LocalDate.now(),
            destino.id()
        );

        return aResponse(repository.guardar(actualizado));
    }

    public void eliminar(Long id, UsuarioAutenticado usuario) {
        obtenerConAcceso(id, usuario);
        repository.eliminar(id);
    }

    // Busca el favorito (404 si no existe) y verifica que su lista sea del usuario (403 si no)
    private Favorito obtenerConAcceso(Long id, UsuarioAutenticado usuario) {
        Favorito favorito = repository.buscarPorId(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "No existe el Favorito " + id
            ));

        listaService.buscar(favorito.listaId(), usuario);
        return favorito;
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