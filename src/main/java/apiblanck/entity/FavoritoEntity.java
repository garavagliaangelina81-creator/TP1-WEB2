package apiblanck.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "favoritos")
public class FavoritoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(name = "nota")
    private String nota;

    @Column(name = "fecha_alta", nullable = false)
    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lista_id")
    private ListaEntity lista;

    // Constructor vacío requerido por JPA
    protected FavoritoEntity() {
    }

    public FavoritoEntity(
        Long id,
        Long productoId,
        String nota,
        LocalDate fecha,
        ListaEntity lista
    ) {
        this.id = id;
        this.productoId = productoId;
        this.nota = nota;
        this.fecha = fecha;
        this.lista = lista;
    }

    public Long getId() {
        return id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public String getNota() {
        return nota;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public ListaEntity getLista() {
        return lista;
    }
}
 /* pq no hacemos un record? pq jpa necesita una entidad mutable, con constructor sin
 argumwntos y atributos que pueda gestionar hbernate */