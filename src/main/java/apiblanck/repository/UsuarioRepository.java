package apiblanck.repository;

import apiblanck.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository {

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> buscarPorEmail(String email);

    boolean existePorEmail(String email);

    Usuario guardar(Usuario usuario);
}
