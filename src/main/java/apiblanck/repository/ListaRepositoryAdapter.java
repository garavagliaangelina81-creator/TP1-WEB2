package apiblanck.repository;

import apiblanck.entity.ListaEntity;
import apiblanck.entity.UsuarioEntity;
import apiblanck.model.Lista;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class ListaRepositoryAdapter implements ListaRepository {

    private final ListaJpaRepository jpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;

    public ListaRepositoryAdapter(
        ListaJpaRepository jpaRepository,
        UsuarioJpaRepository usuarioJpaRepository
    ) {
        this.jpaRepository = jpaRepository;
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public List<Lista> buscarTodos() {
        return jpaRepository.findAll()
            .stream()
            .map(this::aDominio)
            .toList();
    }

    @Override
    public List<Lista> buscarPorUsuarioId(Long usuarioId) {
        return jpaRepository.findByUsuarioId(usuarioId)
            .stream()
            .map(this::aDominio)
            .toList();
    }

    @Override
    public Optional<Lista> buscarPorId(Long id) {
        return jpaRepository.findById(id)
            .map(this::aDominio);
    }

    @Override
    public Lista guardar(Lista lista) {
        ListaEntity entity = aEntity(lista);
        ListaEntity guardada = jpaRepository.save(entity);

        return aDominio(guardada);
    }

    @Override
    public void eliminar(Long id) {
        jpaRepository.deleteById(id);
    }

    private Lista aDominio(ListaEntity entity) {
        return new Lista(
            entity.getId(),
            entity.getNombre(),
            entity.getUsuario().getId()
        );
    }

    private ListaEntity aEntity(Lista lista) {
        UsuarioEntity usuario = usuarioJpaRepository.getReferenceById(lista.usuarioId());
        return new ListaEntity(
            lista.id(),
            lista.nombre(),
            usuario
        );
    }
}
// Igual q el adaptador Favorito. el adaptador se encarga de convertir Lista <> ListaEntity