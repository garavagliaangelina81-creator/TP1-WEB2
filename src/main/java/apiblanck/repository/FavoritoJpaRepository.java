package apiblanck.repository;

import apiblanck.entity.FavoritoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FavoritoJpaRepository extends JpaRepository<FavoritoEntity, Long> {
 List<FavoritoEntity> findByListaId(Long listaId);}

/* aca sprring data jpa nos va a dar automaticamnente operaciones como:
findall, findbyid, save, deletebyid */

/*Sptring Data JPA interpreta findybylistaid como una busqueda por el id de la listaenity asociada. */