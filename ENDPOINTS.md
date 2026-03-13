# TattooAge API — Endpoints

**Base URL:** `https://tattooage-backend-293514144387.europe-west1.run.app`

---

## AUTH — `/api/auth`

### POST /api/auth/register
Registra un nuevo usuario en el sistema. Devuelve un JWT token junto con los datos del usuario creado.

**URL:** `https://tattooage-backend-293514144387.europe-west1.run.app/api/auth/register`

**Body (raw JSON):**
```json
{
  "nombre": "Juan",
  "apellidos": "García López",
  "email": "juan@example.com",
  "password": "password123",
  "telefono": "612345678",
  "rol": "CLIENTE"
}
```
> `rol` es opcional. Valores posibles: `CLIENTE`, `ARTISTA`, `ADMIN`. Por defecto: `CLIENTE`.
> `apellidos` y `telefono` son opcionales.

---

### POST /api/auth/login
Autentica al usuario con email y contraseña. Devuelve un JWT token si las credenciales son válidas.

**URL:** `https://tattooage-backend-293514144387.europe-west1.run.app/api/auth/login`

**Body (raw JSON):**
```json
{
  "email": "juan@example.com",
  "password": "password123"
}
```

---

## PUBLICACIONES — `/api/publicaciones`

### GET /api/publicaciones
Devuelve la lista de todas las publicaciones.

**URL:** `https://tattooage-backend-293514144387.europe-west1.run.app/api/publicaciones`

---

### GET /api/publicaciones/{id}
Devuelve una publicación concreta por su ID. Devuelve 404 si no existe.

**URL:** `https://tattooage-backend-293514144387.europe-west1.run.app/api/publicaciones/1`

---

### DELETE /api/publicaciones/{id}
Elimina una publicación por su ID. Devuelve 204 No Content si se elimina correctamente, 404 si no existe.

**URL:** `https://tattooage-backend-293514144387.europe-west1.run.app/api/publicaciones/1`

---

## ARTISTAS — `/api/artistas`

### GET /api/artistas
Devuelve la lista de todos los usuarios con rol ARTISTA.

**URL:** `https://tattooage-backend-293514144387.europe-west1.run.app/api/artistas`

---

### GET /api/artistas/{id}
Devuelve un artista concreto por su ID de usuario.

**URL:** `https://tattooage-backend-293514144387.europe-west1.run.app/api/artistas/1`

---

### GET /api/artistas/{id}/perfil
Devuelve el perfil detallado (PerfilArtista) asociado al artista con el ID de usuario dado.

**URL:** `https://tattooage-backend-293514144387.europe-west1.run.app/api/artistas/1/perfil`

---

## CITAS — `/api/citas`

### GET /api/citas
Devuelve la lista de todas las citas.

**URL:** `https://tattooage-backend-293514144387.europe-west1.run.app/api/citas`

---

### GET /api/citas/{id}
Devuelve una cita concreta por su ID. Devuelve 404 si no existe.

**URL:** `https://tattooage-backend-293514144387.europe-west1.run.app/api/citas/1`

---

### POST /api/citas
Crea una nueva cita. Devuelve 201 Created con la cita creada.

**URL:** `https://tattooage-backend-293514144387.europe-west1.run.app/api/citas`

**Body (raw JSON):**
```json
{
  "cliente": { "idUsuario": 1 },
  "artista": { "idUsuario": 2 },
  "fechaCita": "2025-06-15",
  "horaInicio": "10:00:00",
  "duracionAproximada": 120,
  "precio": 150.00,
  "estado": "Pendiente",
  "sala": "Sala 1",
  "fotoDiseno": "https://url-imagen.com/diseno.jpg",
  "notas": "Cliente quiere un diseño minimalista en el antebrazo"
}
```
> `estado` es opcional. Valores posibles: `Pendiente`, `Confirmada`, `Cancelada`, `Completada`. Por defecto: `Pendiente`.
> `horaInicio`, `duracionAproximada`, `precio`, `sala`, `fotoDiseno` y `notas` son opcionales.

---

### DELETE /api/citas/{id}
Elimina una cita por su ID. Devuelve 204 No Content si se elimina correctamente, 404 si no existe.

**URL:** `https://tattooage-backend-293514144387.europe-west1.run.app/api/citas/1`

---

## SOLICITUDES DE CITA — `/api/solicitudes`

### GET /api/solicitudes
Devuelve la lista de todas las solicitudes de cita.

**URL:** `https://tattooage-backend-293514144387.europe-west1.run.app/api/solicitudes`

---

### GET /api/solicitudes/{id}
Devuelve una solicitud concreta por su ID. Devuelve 404 si no existe.

**URL:** `https://tattooage-backend-293514144387.europe-west1.run.app/api/solicitudes/1`

---

### POST /api/solicitudes
Crea una nueva solicitud de cita. Devuelve 201 Created con la solicitud creada.

**URL:** `https://tattooage-backend-293514144387.europe-west1.run.app/api/solicitudes`

**Body (raw JSON):**
```json
{
  "cliente": { "idUsuario": 1 },
  "artista": { "idUsuario": 2 },
  "descripcion": "Quiero un tatuaje de una rosa en el hombro derecho",
  "zonaCuerpo": "hombro",
  "tamano": "mediano",
  "presupuestoAprox": 200.00,
  "fechaPreferida": "2025-06-20",
  "fotoReferencia": "https://url-imagen.com/referencia.jpg",
  "notasArtista": "Disponible solo por las mañanas"
}
```
> `zonaCuerpo`, `tamano`, `presupuestoAprox`, `fechaPreferida`, `fotoReferencia` y `notasArtista` son opcionales.
> `estado` se asigna automáticamente como `Pendiente`.

---

### PATCH /api/solicitudes/{id}/estado
Actualiza el estado de una solicitud de cita. Se pasa el nuevo estado como **query parameter**.

**URL:** `https://tattooage-backend-293514144387.europe-west1.run.app/api/solicitudes/1/estado?estado=Aceptada`

> Valores posibles para `estado`: `Pendiente`, `Aceptada`, `Rechazada`, `Completada`.
> No lleva body, solo el query param `estado` en la URL.

---

## Resumen

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/register` | Registrar usuario |
| POST | `/api/auth/login` | Iniciar sesión |
| GET | `/api/publicaciones` | Listar publicaciones |
| GET | `/api/publicaciones/{id}` | Obtener publicación por ID |
| DELETE | `/api/publicaciones/{id}` | Eliminar publicación |
| GET | `/api/artistas` | Listar artistas |
| GET | `/api/artistas/{id}` | Obtener artista por ID |
| GET | `/api/artistas/{id}/perfil` | Obtener perfil del artista |
| GET | `/api/citas` | Listar citas |
| GET | `/api/citas/{id}` | Obtener cita por ID |
| POST | `/api/citas` | Crear cita |
| DELETE | `/api/citas/{id}` | Eliminar cita |
| GET | `/api/solicitudes` | Listar solicitudes |
| GET | `/api/solicitudes/{id}` | Obtener solicitud por ID |
| POST | `/api/solicitudes` | Crear solicitud |
| PATCH | `/api/solicitudes/{id}/estado?estado=` | Actualizar estado de solicitud |
