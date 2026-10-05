package apiblanck.repository;

import apiblanck.model.Lista;

import java.util.List;
import java.util.Optional;

public interface ListaRepository {

    List<Lista> buscarTodos();

    Optional<Lista> buscarPorId(Long id);

    Lista guardar(Lista lista);

    void eliminar(Long id);
}

//esta interrfaz es el puerto de persistencia de las listas, igual que favoritoRepository.