-- Cada lista pertenece a un usuario
ALTER TABLE listas
ADD COLUMN usuario_id BIGINT;

-- Las listas que ya existían (ej: 'Sin clasificar') no tienen dueño real.
-- Se las asigna a un usuario de sistema. Su password_hash ('!') no es un hash
-- BCrypt válido, así que nadie puede iniciar sesión con esa cuenta.
INSERT INTO usuarios (email, password_hash, rol)
VALUES ('sistema@local', '!', 'ADMIN');

UPDATE listas
SET usuario_id = (SELECT id FROM usuarios WHERE email = 'sistema@local');

ALTER TABLE listas
ALTER COLUMN usuario_id SET NOT NULL;

ALTER TABLE listas
ADD CONSTRAINT fk_listas_usuario
FOREIGN KEY (usuario_id) REFERENCES usuarios(id);

-- primero la columna nullable, después se llenan los datos existentes, y recién ahí se pone NOT NULL. Si lo hicieras de una, fallaría porque tu base ya tiene listas.