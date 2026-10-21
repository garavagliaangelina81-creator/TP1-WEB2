package apiblanck.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "listas")
public class ListaEntity {
     @ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "usuario_id", nullable = false)
private UsuarioEntity usuario;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;
    
   

    // Constructor vacío requerido por JPA
    protected ListaEntity() {
    }

    public ListaEntity(Long id, String nombre, UsuarioEntity usuario) {
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
    }
    public UsuarioEntity getUsuario(){
        return usuario;
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