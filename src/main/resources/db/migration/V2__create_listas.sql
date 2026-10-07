-- Creación de la tabla listas
CREATE TABLE listas (
    -- Identificador único de la lista
    id BIGSERIAL PRIMARY KEY,

    -- Nombre de la lista
    nombre VARCHAR(255) NOT NULL
);