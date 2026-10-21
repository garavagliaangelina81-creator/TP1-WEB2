package apiblanck.repository;

import apiblanck.entity.ListaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ListaJpaRepository extends JpaRepository<ListaEntity, Long> {

    List<ListaEntity> findByUsuarioId(Long usuarioId);
}
/* esto hace q spring data JPA nos proporcione autom operaciones basicas de listEntity, buscar,
buscar por id, guardar, eliminar */