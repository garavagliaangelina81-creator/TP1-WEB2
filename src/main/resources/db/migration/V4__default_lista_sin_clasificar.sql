-- Crea la lista por defecto si todavía no existe
INSERT INTO listas (nombre)
SELECT 'Sin clasificar'
WHERE NOT EXISTS (
    SELECT 1
    FROM listas
    WHERE nombre = 'Sin clasificar'
);

-- Asigna los favoritos que todavía no tienen lista
UPDATE favoritos
SET lista_id = (
    SELECT id
    FROM listas
    WHERE nombre = 'Sin clasificar'
    LIMIT 1
)
WHERE lista_id IS NULL;

-- A partir de ahora todos los favoritos deben tener una lista
ALTER TABLE favoritos
ALTER COLUMN lista_id SET NOT NULL;