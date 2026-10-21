package apiblanck.entity;

import apiblanck.model.Rol;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    // Constructor vacío requerido por JPA
    protected UsuarioEntity() {
    }

    public UsuarioEntity(Long id, String email, String passwordHash, Rol rol) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Rol getRol() { return rol; }
}

/*@Enumerated(EnumType.STRING) guarda "USER" / "ADMIN" como texto. Sin eso JPA guarda el número de orden, y si después cambiás el orden del enum se rompen los datos.  */
