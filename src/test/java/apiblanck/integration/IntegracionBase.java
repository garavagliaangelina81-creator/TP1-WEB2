package apiblanck.integration;

import apiblanck.model.Rol;
import apiblanck.model.Usuario;
import apiblanck.repository.UsuarioRepository;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
abstract class IntegracionBase {

    // Un único PostgreSQL real compartido por todas las clases de test
    static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:17");

    static {
        POSTGRES.start();
    }

    // Reemplaza la conexión de application.properties por la del contenedor
    @DynamicPropertySource
    static void propiedadesDeBase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    protected static final String PASSWORD = "secreta123";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Un email distinto por test, así los tests no se pisan entre sí
    protected String emailUnico() {
        return "user-" + UUID.randomUUID() + "@test.com";
    }

    protected String credenciales(String email, String password) {
        return "{\"email\":\"%s\",\"password\":\"%s\"}".formatted(email, password);
    }

    /** Registra un usuario y devuelve su id. */
    protected long registrar(String email) throws Exception {
        String body = mockMvc.perform(post("/api/auth/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credenciales(email, PASSWORD)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(body, "$.id")).longValue();
    }

    /** Hace login y devuelve el JWT. */
    protected String login(String email) throws Exception {
        String body = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credenciales(email, PASSWORD)))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.token");
    }

    /** Registra un usuario nuevo y devuelve su token. */
    protected String nuevoUsuarioConToken() throws Exception {
        String email = emailUnico();
        registrar(email);
        return login(email);
    }

    /** Registra un usuario, lo promueve a ADMIN en la base y devuelve su token. */
    protected String nuevoAdminConToken() throws Exception {
        String email = emailUnico();
        registrar(email);

        // El registro siempre crea USER: lo promovemos directo en la base
        Usuario u = usuarioRepository.buscarPorEmail(email).orElseThrow();
        usuarioRepository.guardar(new Usuario(u.id(), u.email(), u.passwordHash(), Rol.ADMIN));

        // Hay que volver a hacer login: el rol viaja dentro del token
        return login(email);
    }

    /** Crea una lista con el token indicado y devuelve su id. */
    protected long crearLista(String token, String nombre) throws Exception {
        String body = mockMvc.perform(post("/api/listas")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.TEXT_PLAIN)
                .content(nombre))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(body, "$.id")).longValue();
    }
}