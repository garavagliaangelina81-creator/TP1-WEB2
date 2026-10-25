package apiblanck.service;

import apiblanck.exception.CredencialesInvalidasException;
import apiblanck.exception.RecursoEnConflictoException;
import apiblanck.model.Rol;
import apiblanck.model.Usuario;
import apiblanck.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    private UsuarioService service;

    @BeforeEach
    void setUp() {
        service = new UsuarioService(repository, encoder);
    }

    @Test
    void registrar_nuncaGuardaLaContraseniaEnTextoPlano() {
        when(repository.existePorEmail("ana@test.com")).thenReturn(false);
        when(repository.guardar(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        service.registrar("Ana@Test.com", "secreta123");

        // Capturamos el usuario que el service le pasó al repositorio
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(repository).guardar(captor.capture());
        Usuario guardado = captor.getValue();

        assertNotEquals("secreta123", guardado.passwordHash());
        assertTrue(encoder.matches("secreta123", guardado.passwordHash()));
        assertEquals("ana@test.com", guardado.email());
        assertEquals(Rol.USER, guardado.rol());
    }

    @Test
    void registrar_conEmailExistenteLanzaConflicto() {
        when(repository.existePorEmail("ana@test.com")).thenReturn(true);

        assertThrows(RecursoEnConflictoException.class,
            () -> service.registrar("ana@test.com", "secreta123"));

        verify(repository, never()).guardar(any());
    }

    @Test
    void autenticar_conContraseniaIncorrectaLanzaCredencialesInvalidas() {
        Usuario existente = new Usuario(1L, "ana@test.com", encoder.encode("secreta123"), Rol.USER);
        when(repository.buscarPorEmail("ana@test.com")).thenReturn(Optional.of(existente));

        assertThrows(CredencialesInvalidasException.class,
            () -> service.autenticar("ana@test.com", "otraClave"));
    }
}
