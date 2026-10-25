package apiblanck.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthIntegrationTest extends IntegracionBase {

    @Test
    void login_conCredencialesValidasDevuelve200YUnToken() throws Exception {
        String email = emailUnico();
        registrar(email);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credenciales(email, PASSWORD)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token", not(emptyString())));
    }

    @Test
    void login_conCredencialesInvalidasDevuelve401() throws Exception {
        String email = emailUnico();
        registrar(email);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credenciales(email, "contraseniaIncorrecta")))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void listarListasSinTokenDevuelve401() throws Exception {
        mockMvc.perform(get("/api/listas"))
            .andExpect(status().isUnauthorized());
    }
}