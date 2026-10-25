package apiblanck.controller;

import apiblanck.dto.CrearFavoritoRequest;
import apiblanck.dto.FavoritoResponse;
import apiblanck.service.FavoritoService;
import java.util.List;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import apiblanck.model.UsuarioAutenticado;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController 
@RequestMapping("/api/favoritos")
@Tag(name = "Favoritos", description = "Operaciones CRUD sobre la lista de favoritos en memoria")
public class FavoritoController {

    private final FavoritoService service; 

    public FavoritoController(FavoritoService service) {
        this.service = service;
    }

   @GetMapping
@Operation(summary = "Listar favoritos",
    description = "Requiere autenticación. Lista los favoritos de las listas del usuario (todos, si es ADMIN)")
public List<FavoritoResponse> listar(
        @RequestParam(defaultValue = "") String q,
        @Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuario) {
    return service.listar(q, usuario);
}

@GetMapping("/{id}")
@Operation(summary = "Buscar por Id",
    description = "Requiere autenticación. 403 si el favorito pertenece a la lista de otro usuario")
public FavoritoResponse buscar(
        @PathVariable Long id,
        @Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuario) {
    return service.buscar(id, usuario);
}

@PostMapping
@Operation(summary = "Agregar a favoritos",
    description = "Requiere autenticación. Agrega un producto a una lista propia, con una nota personal")
public ResponseEntity<FavoritoResponse> crear(
        @Valid @RequestBody CrearFavoritoRequest request,
        @Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuario) {
    FavoritoResponse creado = service.crear(request, usuario);
    return ResponseEntity.status(HttpStatus.CREATED).body(creado);
}

@PutMapping("/{id}")
@Operation(summary = "Modifica un favorito",
    description = "Requiere autenticación. Permite actualizar un favorito propio")
public FavoritoResponse actualizar(
        @PathVariable Long id,
        @Valid @RequestBody CrearFavoritoRequest request,
        @Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuario) {
    return service.actualizar(id, request, usuario);
}

@DeleteMapping("/{id}")
@Operation(summary = "Eliminar favorito",
    description = "Requiere autenticación. Permite eliminar un favorito propio")
public ResponseEntity<Void> eliminar(
        @PathVariable Long id,
        @Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuario) {
    service.eliminar(id, usuario);
    return ResponseEntity.noContent().build();
}
}
//Es el mas complejo de los tres, es el que se comunica con el cliente, recibe las solicitudes y devuelve las respuestas.
/*Usa muchos anotadores:
-@RestController: indica que es un controlador REST, es decir, que devuelve datos en formato JSON.
-@RequestMapping("/api/Favoritos"): indica la ruta base de las solicitudes, es decir, que todas las solicitudes que empiecen con /api/Favoritos se van a manejar en este controlador.
-@GetMapping: indica que el metodo maneja solicitudes GET, es decir, que devuelve datos.
-@PostMapping: indica que el metodo maneja solicitudes POST, es decir, que recibe datos.
-@PutMapping: indica que el metodo maneja solicitudes PUT, es decir, que actualiza datos.
-@DeleteMapping: indica que el metodo maneja solicitudes DELETE, es decir, que elimina datos.
-ResponseEntity: es una clase que representa una respuesta HTTP, es decir, que contiene el codigo de estado, los encabezados y el cuerpo de la respuesta.
*/