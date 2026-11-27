# PRUEBAS - Llave Maestra

Las siguientes pruebas se ejecutan directamente en Swagger, sin utilizar Angular.

| N.º | Usuario | Caso | Entrada | Resultado esperado | Resultado obtenido |
|---|---|---|---|---|---|
| 1 | Pedro | Crear alquiler válido | Herramienta activa y fechas válidas | HTTP 201 - Alquiler creado | Pendiente de ejecutar en Swagger |
| 2 | Pedro | Alquiler de 15 días exactos | Fecha inicial y final con 15 días inclusivos | HTTP 201 - Alquiler creado | Pendiente de ejecutar en Swagger |
| 3 | Pedro | Alquiler de 16 días | Rango de 16 días inclusivos | HTTP 400 - Rechazo por superar el máximo | Pendiente de ejecutar en Swagger |
| 4 | Pedro | Fecha inicial pasada | Fecha inicial anterior al día actual | HTTP 400 - Fecha inválida | Pendiente de ejecutar en Swagger |
| 5 | Pedro | Fechas invertidas | Fecha final anterior a la inicial | HTTP 400 - Rango inválido | Pendiente de ejecutar en Swagger |
| 6 | Pedro | Consultar sus alquileres | GET /api/alquileres/mios | HTTP 200 - Solo sus alquileres | Pendiente de ejecutar en Swagger |
| 7 | Pedro | Intentar alquilar fechas ocupadas | Misma herramienta y fechas solapadas | HTTP 409 - Conflicto de disponibilidad | Pendiente de ejecutar en Swagger |
| 8 | Pedro | Alquiler justo después de otro | Inicio al día siguiente de finalizar el anterior | HTTP 201 - Alquiler permitido | Pendiente de ejecutar en Swagger |
| 9 | Pedro | Superar máximo de alquileres activos | Intentar crear un cuarto alquiler activo | HTTP 400 - Límite de alquileres alcanzado | Pendiente de ejecutar en Swagger |
| 10 | Diana | Consultar sus alquileres | GET /api/alquileres/mios con JWT de Diana | HTTP 200 - Solo alquileres de Diana | Pendiente de ejecutar en Swagger |
| 11 | Diana | Cancelar alquiler de otro usuario | PATCH /api/alquileres/{id}/cancelar con ID ajeno | HTTP 403 - Operación no autorizada | Pendiente de ejecutar en Swagger |
| 12 | Hernán | Gestionar alquiler | Operación permitida con JWT y permiso de gestión | HTTP 200 - Operación autorizada | Pendiente de ejecutar en Swagger |

