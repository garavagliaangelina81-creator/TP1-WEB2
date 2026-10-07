package apiblanck.controller;

import apiblanck.model.Lista;
import apiblanck.service.ListaService;
import apiblanck.dto.MoverFavoritosRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/listas")
@Tag(name = "Listas", description = "Operaciones sobre las listas de favoritos")
public class ListaController {

    private final ListaService service;

    public ListaController(ListaService service) {
        this.service = service;
    }

    @GetMapping
    @Operation( summary = "Listar listas", 
    description = "Lista todas las listas de favoritos que se crearon")
    public ResponseEntity<List<Lista>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation( summary = "Buscar lista por ID", 
    description = "Busca una lista de favoritos por su ID")
    public ResponseEntity<Lista> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @PostMapping
    @Operation( 
        summary = "Crear lista",
        description = "Crea una nueva lista de favoritos con el nombre especificado")
    public ResponseEntity<Lista> crear(@RequestBody String nombre) {
        return ResponseEntity.ok(service.crear(nombre));
    }
    @GetMapping("/{id}/favoritos")
    @Operation( summary = "Listar favoritos de una lista", 
    description = "Lista todos los favoritos de una lista específica")
public ResponseEntity<List<apiblanck.model.Favorito>> listarFavoritos(
    @PathVariable Long id
) {
    return ResponseEntity.ok(service.listarFavoritos(id));
}

@DeleteMapping("/{id}")
@Operation (
    summary = "Eliminar lista",
    description = "Elimina una lista vacía. Si contiene favoritos, devuelve 409 Conflict"
)
public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    service.eliminar(id);
    return ResponseEntity.noContent().build();
}

@PostMapping("/{origenId}/mover-favoritos")
@Operation(
    summary = "Mover favoritos de una lista a otra",
    description = "Mueve todos los favoritos de la lista origen a la lista destino y elimina la lista origen"
)
public ResponseEntity<Void> moverFavoritos(
    @PathVariable Long origenId,
    @Valid @RequestBody MoverFavoritosRequest request
) {
    service.moverFavoritos(origenId, request.destinoId());
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