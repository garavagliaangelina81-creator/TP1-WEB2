package apiblanck.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestClientConfig {
    @Bean 
    public RestClient dummyJsonRestClient(@Value("${app.dummyjson.base-url}") String baseUrl) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
/**
 * bean de restClient apuntando a la api de dummyjson. se inyecta en 
 * dummyjsonclient - es la unica clase que lo usa.
 * para practicar: se puede endurecer con timeouts propios pasando un 
 * clientehttprequestFactory a requestFactory en vez de usar el tra por defecto
 */