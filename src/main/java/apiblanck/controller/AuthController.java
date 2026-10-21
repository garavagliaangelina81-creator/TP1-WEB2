package apiblanck.controller;

import apiblanck.dto.RegistroRequest;
import apiblanck.dto.UsuarioResponse;
import apiblanck.model.Usuario;
import apiblanck.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import apiblanck.dto.LoginRequest;
import apiblanck.dto.TokenResponse;
import apiblanck.security.JwtService;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Registro e inicio de sesión (endpoints públicos)")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public AuthController(UsuarioService usuarioService, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    @PostMapping("/registro")
    @Operation(
        summary = "Registrar usuario",
        description = "Crea un usuario con rol USER. La contraseña se guarda hasheada. Responde 409 si el email ya existe"
    )
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        Usuario usuario = usuarioService.registrar(request.email(), request.password());
        UsuarioResponse body = new UsuarioResponse(usuario.id(), usuario.email(), usuario.rol());
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
    @PostMapping("/login")
@Operation(
    summary = "Iniciar sesión",
    description = "Valida email y contraseña y devuelve un JWT. Responde 401 si las credenciales son inválidas"
)
public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
    Usuario usuario = usuarioService.autenticar(request.email(), request.password());
    String token = jwtService.generarToken(usuario);
    return ResponseEntity.ok(new TokenResponse(token, "Bearer", jwtService.duracionEnSegundos()));
}
}
