package apiblanck.service;

import apiblanck.exception.CredencialesInvalidasException;
import apiblanck.exception.RecursoEnConflictoException;
import apiblanck.model.Rol;
import apiblanck.model.Usuario;
import apiblanck.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrar(String email, String passwordPlano) {
        String emailNormalizado = email.trim().toLowerCase();

        if (repository.existePorEmail(emailNormalizado)) {
            throw new RecursoEnConflictoException(
                "Ya existe un usuario con el email " + emailNormalizado
            );
        }

        // Se guarda SOLO el hash, nunca la contraseña en texto plano
        Usuario nuevo = new Usuario(
            null,
            emailNormalizado,
            passwordEncoder.encode(passwordPlano),
            Rol.USER
        );

        return repository.guardar(nuevo);
    }
    public Usuario autenticar(String email, String passwordPlano) {
    // Mismo error si el email no existe o la contraseña es incorrecta: no revela cuál falló
    Usuario usuario = repository.buscarPorEmail(email.trim().toLowerCase())
        .orElseThrow(CredencialesInvalidasException::new);

    if (!passwordEncoder.matches(passwordPlano, usuario.passwordHash())) {
        throw new CredencialesInvalidasException();
    }

    return usuario;


}
}

/* passwordEncoder.matches compara la contraseña en texto plano contra el hash guardado. Nunca se “desencripta” nada: BCrypt es de un solo sentido. */