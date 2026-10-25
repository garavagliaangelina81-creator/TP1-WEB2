package apiblanck.service;

import apiblanck.exception.AccesoDenegadoException;
import apiblanck.model.Lista;
import apiblanck.model.Rol;
import apiblanck.model.UsuarioAutenticado;
import apiblanck.repository.FavoritoRepository;
import apiblanck.repository.ListaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListaServiceTest {

    @Mock
    private ListaRepository listaRepository;

    @Mock
    private FavoritoRepository favoritoRepository;

    private ListaService service;

    private final UsuarioAutenticado ana = new UsuarioAutenticado(1L, "ana@test.com", Rol.USER);
    private final UsuarioAutenticado beto = new UsuarioAutenticado(2L, "beto@test.com", Rol.USER);
    private final UsuarioAutenticado admin = new UsuarioAutenticado(99L, "admin@test.com", Rol.ADMIN);

    @BeforeEach
    void setUp() {
        service = new ListaService(listaRepository, favoritoRepository);
    }

    @Test
    void crear_asociaLaListaAlUsuarioAutenticado() {
        when(listaRepository.guardar(any(Lista.class))).thenAnswer(inv -> inv.getArgument(0));

        service.crear("Trabajo", ana);

        ArgumentCaptor<Lista> captor = ArgumentCaptor.forClass(Lista.class);
        verify(listaRepository).guardar(captor.capture());
        assertEquals(ana.id(), captor.getValue().usuarioId());
        assertEquals("Trabajo", captor.getValue().nombre());
    }

    @Test
    void buscar_listaAjenaLanzaAccesoDenegado() {
        when(listaRepository.buscarPorId(10L))
            .thenReturn(Optional.of(new Lista(10L, "De Ana", ana.id())));

        assertThrows(AccesoDenegadoException.class, () -> service.buscar(10L, beto));
    }

    @Test
    void buscar_listaAjenaSiEsAdminLaDevuelve() {
        Lista lista = new Lista(10L, "De Ana", ana.id());
        when(listaRepository.buscarPorId(10L)).thenReturn(Optional.of(lista));

        assertEquals(lista, service.buscar(10L, admin));
    }
}