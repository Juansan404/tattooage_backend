CREATE TABLE IF NOT EXISTS valoraciones (
    id_valoracion  SERIAL PRIMARY KEY,
    id_artista     INTEGER NOT NULL REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    id_cliente     INTEGER NOT NULL REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    puntuacion     INTEGER NOT NULL CHECK (puntuacion BETWEEN 1 AND 5),
    comentario     TEXT,
    creado_en      TIMESTAMP DEFAULT NOW(),
    CONSTRAINT uq_valoracion_artista_cliente UNIQUE (id_artista, id_cliente)
);

CREATE INDEX IF NOT EXISTS idx_valoraciones_artista ON valoraciones (id_artista);
CREATE INDEX IF NOT EXISTS idx_valoraciones_cliente ON valoraciones (id_cliente);
