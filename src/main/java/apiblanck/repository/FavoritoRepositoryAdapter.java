package apiblanck.repository;

import apiblanck.entity.FavoritoEntity;
import apiblanck.model.Favorito;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class FavoritoRepositoryAdapter implements FavoritoRepository {

    private final FavoritoJpaRepository jpaRepository;

    public FavoritoRepositoryAdapter(FavoritoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
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
        FavoritoEntity entity = aEntity(favorito);
        FavoritoEntity guardado = jpaRepository.save(entity);

        return aDominio(guardado);
    }

    @Override
    public void eliminar(Long id) {
        jpaRepository.deleteById(id);
    }

    private Favorito aDominio(FavoritoEntity entity) {
        return new Favorito(
            entity.getId(),
            entity.getProductoId(),
            entity.getNota(),
            entity.getFecha()
        );
    }

    private FavoritoEntity aEntity(Favorito favorito) {
        return new FavoritoEntity(
            favorito.id(),
            favorito.productoId(),
            favorito.nota(),
            favorito.fecha()
        );
    }
}