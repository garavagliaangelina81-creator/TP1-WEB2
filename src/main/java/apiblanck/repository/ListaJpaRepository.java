package apiblanck.repository;

import apiblanck.entity.ListaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListaJpaRepository extends JpaRepository<ListaEntity, Long> {
}
/* esto hace q spring data JPA nos proporcione autom operaciones basicas de listEntity, buscar,
buscar por id, guardar, eliminar */