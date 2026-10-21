package apiblanck.repository;

import apiblanck.entity.ListaEntity;
import apiblanck.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long> {

     List<ListaEntity> findByUsuarioId(Long usuarioId);
    Optional<UsuarioEntity> findByEmail(String email);
   

    boolean existsByEmail(String email);
}