package apiblanck.security;

import apiblanck.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import apiblanck.model.Rol;
import apiblanck.model.UsuarioAutenticado;
import io.jsonwebtoken.Claims;

@Service
public class JwtService {

    private final SecretKey clave;
    private final Duration duracion;

    public JwtService(
        @Value("${app.jwt.secret}") String secreto,
        @Value("${app.jwt.expiration-minutes}") long minutos
    ) {
        byte[] bytes = secreto.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException("JWT_SECRET debe tener al menos 32 caracteres");
        }
        this.clave = Keys.hmacShaKeyFor(bytes);
        this.duracion = Duration.ofMinutes(minutos);
    }

    public String generarToken(Usuario usuario) {
        Instant ahora = Instant.now();
        return Jwts.builder()
            .subject(String.valueOf(usuario.id()))
            .claim("email", usuario.email())
            .claim("rol", usuario.rol().name())
            .issuedAt(Date.from(ahora))
            .expiration(Date.from(ahora.plus(duracion)))
            .signWith(clave)
            .compact();
    }

    public long duracionEnSegundos() {
        return duracion.toSeconds();
    }
    /** Devuelve la identidad del token, o lanza una excepción si es inválido o está vencido. */
public UsuarioAutenticado validar(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(clave)
        .build()
        .parseSignedClaims(token)
        .getPayload();

    return new UsuarioAutenticado(
        Long.valueOf(claims.getSubject()),
        claims.get("email", String.class),
        Rol.valueOf(claims.get("rol", String.class))
    );
}
}

/* parseSignedClaims verifica la firma y la fecha de vencimiento. Si algo no cuadra, lanza una excepción (JwtException y derivadas), que el filtro va a atrapar.  */