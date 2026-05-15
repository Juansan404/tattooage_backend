-- Migración 011: Añadir web, instagram y localizacion a estudios
--                + limpiar la tabla empresas si existe

ALTER TABLE estudios
    ADD COLUMN IF NOT EXISTS web          VARCHAR(200),
    ADD COLUMN IF NOT EXISTS instagram    VARCHAR(100),
    ADD COLUMN IF NOT EXISTS localizacion TEXT;

-- Desvincular artistas de empresas antes de eliminar la tabla
ALTER TABLE perfiles_artista
    DROP COLUMN IF EXISTS id_empresa;

DROP TABLE IF EXISTS empresas;
