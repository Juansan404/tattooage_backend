-- Migration 009: Publicaciones guardadas por usuario
CREATE TABLE IF NOT EXISTS publicaciones_guardadas (
    id_guardado    SERIAL PRIMARY KEY,
    id_publicacion INTEGER      NOT NULL REFERENCES publicaciones(id_publicacion) ON DELETE CASCADE,
    id_usuario     INTEGER      NOT NULL REFERENCES usuarios(id_usuario)          ON DELETE CASCADE,
    creado_en      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_guardado UNIQUE (id_publicacion, id_usuario)
);

CREATE INDEX IF NOT EXISTS idx_guardadas_usuario ON publicaciones_guardadas(id_usuario);
