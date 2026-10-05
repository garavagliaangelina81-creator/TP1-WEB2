package apiblanck.repository;

import apiblanck.entity.FavoritoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritoJpaRepository extends JpaRepository<FavoritoEntity, Long> {
}

/* aca sprring data jpa nos va a dar automaticamnente operaciones como:
findall, findbyid, save, deletebyid */