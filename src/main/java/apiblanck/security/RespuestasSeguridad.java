package apiblanck.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class RespuestasSeguridad implements AuthenticationEntryPoint, AccessDeniedHandler {

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException {
        escribir(response, 401, "Unauthorized", "Falta un token válido");
    }

    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException
    ) throws IOException {
        escribir(response, 403, "Forbidden", "No tenés permiso para acceder a este recurso");
    }

    private void escribir(HttpServletResponse response, int status, String titulo, String detalle) throws IOException {
        response.setStatus(status);
        response.setContentType("application/problem+json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
            "{\"title\":\"" + titulo + "\",\"status\":" + status + ",\"detail\":\"" + detalle + "\"}"
        );
    }
}
/*Respuestas 401 y 403 con formato propio

Spring Security, por defecto, responde estos errores con su propio formato. Para que sean coherentes con el resto de tu API */