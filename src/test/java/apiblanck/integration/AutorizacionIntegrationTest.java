package apiblanck.integration;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AutorizacionIntegrationTest extends IntegracionBase {

    private static String bearer(String token) {
        return "Bearer " + token;
    }

    @Test
    void accederAListaPropiaDevuelve200() throws Exception {
        String token = nuevoUsuarioConToken();
        long listaId = crearLista(token, "Propia");

        mockMvc.perform(get("/api/listas/" + listaId)
                .header("Authorization", bearer(token)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Propia"));
    }

    @Test
    void accederAListaAjenaDevuelve403() throws Exception {
        String tokenAna = nuevoUsuarioConToken();
        String tokenBeto = nuevoUsuarioConToken();
        long listaDeAna = crearLista(tokenAna, "De Ana");

        // Beto intenta leer la lista de Ana
        mockMvc.perform(get("/api/listas/" + listaDeAna)
                .header("Authorization", bearer(tokenBeto)))
            .andExpect(status().isForbidden());
    }

    @Test
    void adminAccediendoAListaAjenaDevuelve200() throws Exception {
        String tokenAna = nuevoUsuarioConToken();
        String tokenAdmin = nuevoAdminConToken();
        long listaDeAna = crearLista(tokenAna, "De Ana");

        mockMvc.perform(get("/api/listas/" + listaDeAna)
                .header("Authorization", bearer(tokenAdmin)))
            .andExpect(status().isOk());
    }
}