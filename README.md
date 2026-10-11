# AppDummy v1 — G-APPDUMMY-V1
Valeriy Khokhlov · NIA: 10154580

## Qué hace
Primera versión de AppDummy, una biblioteca personal hecha con Jetpack Compose.
Incluye una pantalla de bienvenida con validación del nombre, un listado de libros
en rejilla con búsqueda, filtro por autor, favoritos, leídos y compartir, y una
pantalla de gestión del permiso de cámara.

## Estructura
- `model/LibroUI.kt`: modelo de datos y lista de libros de ejemplo.
- `screens/PantallaBienvenida.kt`: pantalla de bienvenida con validación del nombre.
- `screens/ItemLibro.kt`: tarjeta de un libro (portada, datos, favorito, leído, compartir).
- `screens/PantallaListado.kt`: rejilla de libros con búsqueda, chips y estado vacío.
- `screens/EstadoPermiso.kt`: sealed class con los estados del permiso.
- `screens/PantallaGestionPermisos.kt`: solicitud y gestión del permiso de cámara.

## Dependencias añadidas
| Librería | Para qué |
|---|---|
| material-icons-extended | Iconos adicionales (Book, SearchOff, BookmarkAdded…) |
| coil-compose | Cargar portadas con AsyncImage |
| coil-network-okhttp | Descargar las portadas desde Internet |

## Permisos declarados
| Permiso | Por qué es necesario |
|---|---|
| INTERNET | Descargar las portadas desde Open Library |
| CAMERA | Fotografiar portadas (gestión del permiso en tiempo de ejecución) |

## Decisiones propias


## Limitaciones conocidas
- Commits de un solo día
