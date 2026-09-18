package apiblanck.client;

public record DummyJsonProducto (
    Long id,
    String title,
    String description,
    String category,
    String brand,
    Double price,
    Double discountPercentage,
    int stock,
    Double rating,
    String thumbnail
) { 

}

/** 
 * representa un producto tal como lo devuelve dummyjson
 * los nombres de campo son los del json
 * externo a proposito; esta clase existe solo para deserializar la respuesta
 * y nunca sale del paquete client. el resto de la app trabaja con com.exampke.demo.producto.productoDto.
 */