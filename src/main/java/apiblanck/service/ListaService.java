package apiblanck.service;

import apiblanck.exception.AccesoDenegadoException;
import apiblanck.exception.RecursoEnConflictoException;
import apiblanck.exception.RecursoNoEncontradoException;
import apiblanck.model.Favorito;
import apiblanck.model.Lista;
import apiblanck.model.UsuarioAutenticado;
import apiblanck.repository.FavoritoRepository;
import apiblanck.repository.ListaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<Lista> listar(UsuarioAutenticado usuario) {
        if (usuario.esAdmin()) {
            return listaRepository.buscarTodos();
        }
        return listaRepository.buscarPorUsuarioId(usuario.id());
    }

    public Lista buscar(Long id, UsuarioAutenticado usuario) {
        Lista lista = obtener(id);
        verificarAcceso(lista, usuario);
        return lista;
    }

    public Lista crear(String nombre, UsuarioAutenticado usuario) {
        // El dueño sale SIEMPRE del token, nunca de lo que mande el cliente
        Lista nueva = new Lista(null, nombre, usuario.id());
        return listaRepository.guardar(nueva);
    }

    public void eliminar(Long id, UsuarioAutenticado usuario) {
        buscar(id, usuario);

        List<Favorito> favoritos = favoritoRepository.buscarPorListaId(id);

        if (!favoritos.isEmpty()) {
            throw new RecursoEnConflictoException(
                "No se puede eliminar la lista porque contiene favoritos"
            );
        }

        listaRepository.eliminar(id);
    }

    public List<Favorito> listarFavoritos(Long listaId, UsuarioAutenticado usuario) {
        buscar(listaId, usuario);

        return favoritoRepository.buscarPorListaId(listaId);
    }

    @Transactional
    public void moverFavoritos(Long origenId, Long destinoId, UsuarioAutenticado usuario) {
        Lista origen = listaRepository.buscarPorId(origenId)
            .orElseThrow(() ->
                new RecursoNoEncontradoException("No existe la lista de origen " + origenId)
            );

        Lista destino = listaRepository.buscarPorId(destinoId)
            .orElseThrow(() ->
                new RecursoNoEncontradoException("No existe la lista de destino " + destinoId)
            );

        // Hay que ser dueño (o ADMIN) de AMBAS listas
        verificarAcceso(origen, usuario);
        verificarAcceso(destino, usuario);

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

    private Lista obtener(Long id) {
        return listaRepository.buscarPorId(id)
            .orElseThrow(() ->
                new RecursoNoEncontradoException("No existe la Lista " + id)
            );
    }

    /**
     * Regla de autorización: dueño o ADMIN.
     * Si no, 403 (no 404: la lista existe, pero no es suya).
     */
    private void verificarAcceso(Lista lista, UsuarioAutenticado usuario) {
        boolean esDuenio = lista.usuarioId().equals(usuario.id());

        if (!esDuenio && !usuario.esAdmin()) {
            throw new AccesoDenegadoException(
                "No tenés permiso para operar sobre la lista " + lista.id()
            );
        }
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
