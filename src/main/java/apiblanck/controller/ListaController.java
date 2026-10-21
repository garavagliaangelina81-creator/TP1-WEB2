package apiblanck.controller;

import apiblanck.dto.MoverFavoritosRequest;
import apiblanck.model.Favorito;
import apiblanck.model.Lista;
import apiblanck.model.UsuarioAutenticado;
import apiblanck.service.ListaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listas")
@Tag(name = "Listas", description = "Operaciones sobre las listas de favoritos. Requiere autenticación")
public class ListaController {

    private final ListaService service;

    public ListaController(ListaService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar listas",
        description = "Requiere autenticación. Devuelve las listas del usuario autenticado (todas, si es ADMIN)")
    public ResponseEntity<List<Lista>> listar(
        @Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuario
    ) {
        return ResponseEntity.ok(service.listar(usuario));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar lista por ID",
        description = "Requiere autenticación. 403 si la lista no es del usuario (salvo ADMIN)")
    public ResponseEntity<Lista> buscar(
        @PathVariable Long id,
        @Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuario
    ) {
        return ResponseEntity.ok(service.buscar(id, usuario));
    }

    @PostMapping
    @Operation(summary = "Crear lista",
        description = "Requiere autenticación. Crea una lista cuyo dueño es el usuario autenticado")
    public ResponseEntity<Lista> crear(
        @RequestBody String nombre,
        @Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuario
    ) {
        return ResponseEntity.ok(service.crear(nombre, usuario));
    }

    @GetMapping("/{id}/favoritos")
    @Operation(summary = "Listar favoritos de una lista",
        description = "Requiere autenticación. Solo de una lista propia (o de cualquiera, si es ADMIN)")
    public ResponseEntity<List<Favorito>> listarFavoritos(
        @PathVariable Long id,
        @Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuario
    ) {
        return ResponseEntity.ok(service.listarFavoritos(id, usuario));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar lista",
        description = "Requiere autenticación. Elimina una lista propia vacía. Si contiene favoritos, 409 Conflict")
    public ResponseEntity<Void> eliminar(
        @PathVariable Long id,
        @Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuario
    ) {
        service.eliminar(id, usuario);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{origenId}/mover-favoritos")
    @Operation(summary = "Mover favoritos de una lista a otra",
        description = "Requiere autenticación. Ambas listas deben ser del usuario (o ser ADMIN)")
    public ResponseEntity<Void> moverFavoritos(
        @PathVariable Long origenId,
        @Valid @RequestBody MoverFavoritosRequest request,
        @Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuario
    ) {
        service.moverFavoritos(origenId, request.destinoId(), usuario);
        return ResponseEntity.noContent().build();
    }
}
/* GET  /api/listas
GET  /api/listas/{id}
POST /api/listas 
GET  /api/listas/{id}/favoritos*/
/* esto queda:

DELETE /api/listas/1 → 204 si la lista está vacía.
Si la lista tiene favoritos → 409 Conflict.
Si la lista no existe → 404 Not Found. */