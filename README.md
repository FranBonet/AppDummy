# AppDummy v1 — G-APPDUMMY-V1

Francisco Manuel Bonet López · 10160293

## Qué hace

La bienvenida valida el nombre y lo conserva al rotar.
El catálogo muestra cinco libros en dos columnas y combina la búsqueda por título y el filtro por autor.
Cada tarjeta permite marcar el libro como favorito o leído y compartir su título y autor.
La pantalla de permisos comprueba y solicita el acceso a la cámara.

## Estructura

- `MainActivity.kt` inicia la bienvenida y aplica el tema de la aplicación.
- `PantallaBienvenida.kt` valida el nombre y conserva el campo al recrear la pantalla.
- `LibroUI.kt` define los datos y los estados de cada libro.
- `PantallaListado.kt` mantiene la colección y aplica la búsqueda y el filtro.
- `ItemLibro.kt` muestra los datos y la portada del libro y ofrece sus tres acciones.
- `PantallaGestionPermisos.kt` muestra el estado del permiso de cámara y abre los ajustes cuando corresponde.

## Dependencias añadidas

| Librería | Para qué |
| --- | --- |
| Material Icons Extended | Iconos de libros, filtros y acciones. |
| Coil Compose y Coil Network OkHttp | Mostrar portadas locales y descargar las remotas. |

## Permisos declarados

| Permiso | Por qué es necesario |
| --- | --- |
| `INTERNET` | Descargar las portadas que tienen URL. |
| `CAMERA` | Practicar la solicitud de acceso a la cámara en tiempo de ejecución. |

## Decisiones propias

1. Validar el nombre tras quitar los espacios de los extremos, para que no cuenten para el mínimo.
2. Dar a las portadas una proporción de 2:3, para conservar espacio para los datos y botones.
3. Volver a comprobar el permiso al regresar de Ajustes, para reflejar el cambio en pantalla.

## Limitaciones conocidas

Las pantallas se ejecutan de forma independiente. La colección es de ejemplo y los cambios de favorito y leído se pierden al cerrar el listado. La pantalla de permisos comprueba el acceso, pero no toma fotos.
