package apiblanck.repository;

import apiblanck.entity.UsuarioEntity;
import apiblanck.model.Usuario;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final UsuarioJpaRepository jpaRepository;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(this::aDominio);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::aDominio);
    }

    @Override
    public boolean existePorEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity(
            usuario.id(),
            usuario.email(),
            usuario.passwordHash(),
            usuario.rol()
        );
        return aDominio(jpaRepository.save(entity));
    }

    private Usuario aDominio(UsuarioEntity entity) {
        return new Usuario(
            entity.getId(),
            entity.getEmail(),
            entity.getPasswordHash(),
            entity.getRol()
        );
    }
}
