package apiblanck.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "listas")
public class ListaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    // Constructor vacío requerido por JPA
    protected ListaEntity() {
    }

    public ListaEntity(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}

// Estamos haciendo nuevamente separacion
// Dominio - lista.java
// Persistencia - listaEntity.java
// postreSQL
// listaentity es la clase Hibernate va a utilizar para representar la tabala listas