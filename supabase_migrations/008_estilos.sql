-- Migration 008: Dynamic tattoo styles catalog
-- Estilos catalog (case-insensitive unique names)
CREATE TABLE IF NOT EXISTS estilos (
    id_estilo SERIAL PRIMARY KEY,
    nombre    VARCHAR(100) NOT NULL,
    CONSTRAINT uq_estilo_nombre UNIQUE (nombre)
);

-- Many-to-many: publication <-> styles
CREATE TABLE IF NOT EXISTS publicacion_estilos (
    id_publicacion INTEGER NOT NULL REFERENCES publicaciones(id_publicacion) ON DELETE CASCADE,
    id_estilo      INTEGER NOT NULL REFERENCES estilos(id_estilo)            ON DELETE CASCADE,
    PRIMARY KEY (id_publicacion, id_estilo)
);

-- Many-to-many: artist profile <-> preferred styles
CREATE TABLE IF NOT EXISTS artista_estilos (
    id_perfil INTEGER NOT NULL REFERENCES perfiles_artista(id_perfil) ON DELETE CASCADE,
    id_estilo INTEGER NOT NULL REFERENCES estilos(id_estilo)          ON DELETE CASCADE,
    PRIMARY KEY (id_perfil, id_estilo)
);

-- Seed: estilos comunes para que el catálogo no esté vacío desde el inicio
INSERT INTO estilos (nombre) VALUES
    ('blackwork'),
    ('realismo'),
    ('tradicional'),
    ('neo-tradicional'),
    ('acuarela'),
    ('minimalista'),
    ('geométrico'),
    ('japonés'),
    ('tribal'),
    ('old school'),
    ('lettering'),
    ('biomecánico'),
    ('trash polka'),
    ('fineline'),
    ('dotwork')
ON CONFLICT (nombre) DO NOTHING;
