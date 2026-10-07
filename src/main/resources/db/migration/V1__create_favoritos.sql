CREATE TABLE favoritos (
    id BIGSERIAL PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    nota VARCHAR(200),
    fecha_alta DATE NOT NULL
);

--antes los favoritos los tenia guardados en memoria. si apagabas la aplicacion, los datos desaparecian
--y FLYWAY se encarga de crear y modificar la estructura de postgresql mediante migraciones.
