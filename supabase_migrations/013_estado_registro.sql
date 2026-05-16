-- Añade columna estado_registro a usuarios para flujo de aprobación de artistas
ALTER TABLE usuarios
ADD COLUMN IF NOT EXISTS estado_registro VARCHAR(20) NOT NULL DEFAULT 'ACTIVO';

-- Los artistas ya registrados que están activos se marcan como ACTIVO
UPDATE usuarios SET estado_registro = 'ACTIVO' WHERE estado_registro = 'ACTIVO';

CREATE INDEX IF NOT EXISTS idx_usuarios_estado_registro ON usuarios (estado_registro);
