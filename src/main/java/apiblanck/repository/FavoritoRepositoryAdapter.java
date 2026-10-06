package apiblanck.repository;

import apiblanck.entity.FavoritoEntity;
import apiblanck.entity.ListaEntity;
import apiblanck.model.Favorito;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class FavoritoRepositoryAdapter implements FavoritoRepository {

    private final FavoritoJpaRepository jpaRepository;
    private final ListaJpaRepository listaJpaRepository;

    public FavoritoRepositoryAdapter(
        FavoritoJpaRepository jpaRepository,
        ListaJpaRepository listaJpaRepository
    ) {
        this.jpaRepository = jpaRepository;
        this.listaJpaRepository = listaJpaRepository;
    }

    @Override
    public List<Favorito> buscarTodos() {
        return jpaRepository.findAll()
            .stream()
            .map(this::aDominio)
            .toList();
    }

    @Override
    public Optional<Favorito> buscarPorId(Long id) {
        return jpaRepository.findById(id)
            .map(this::aDominio);
    }

    @Override
    public Favorito guardar(Favorito favorito) {
        ListaEntity lista = listaJpaRepository.getReferenceById(
            favorito.listaId()
        );

        FavoritoEntity entity = new FavoritoEntity(
            favorito.id(),
            favorito.productoId(),
            favorito.nota(),
            favorito.fecha(),
            lista
        );

        FavoritoEntity guardado = jpaRepository.save(entity);

        return aDominio(guardado);
    }

    @Override
    public void eliminar(Long id) {
        jpaRepository.deleteById(id);
    }
    @Override
    public List<Favorito> buscarPorListaId(Long listaId) {
    return jpaRepository.findByListaId(listaId)
        .stream()
        .map(this::aDominio)
        .toList();
}

    private Favorito aDominio(FavoritoEntity entity) {
        return new Favorito(
            entity.getId(),
            entity.getProductoId(),
            entity.getNota(),
            entity.getFecha(),
            entity.getLista().getId()
        );
    }
}
/* ahora adapter hace la conversion: dominio - JPA y al volver JPA - dominio
usamos getReferenceById() para obtener la referencia JPA de la lista sin tener que cargar toda la entidad. */