-- Tabla empresas
CREATE TABLE IF NOT EXISTS empresas (
    id_empresa   SERIAL PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL,
    descripcion  TEXT,
    ciudad       VARCHAR(100),
    localizacion TEXT,
    email        VARCHAR(100),
    telefono     VARCHAR(15),
    web          VARCHAR(200),
    logo         TEXT,
    creado_en    TIMESTAMP DEFAULT NOW()
);

-- FK en perfiles_artista → empresas (nullable)
ALTER TABLE perfiles_artista
    ADD COLUMN IF NOT EXISTS id_empresa INTEGER REFERENCES empresas(id_empresa) ON DELETE SET NULL;
