-- Agrega la relación entre favoritos y listas
ALTER TABLE favoritos
ADD COLUMN lista_id BIGINT;

-- Clave foránea hacia la tabla listas
ALTER TABLE favoritos
ADD CONSTRAINT fk_favoritos_lista
FOREIGN KEY (lista_id) REFERENCES listas(id);