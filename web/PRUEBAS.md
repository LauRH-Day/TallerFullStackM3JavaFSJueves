
# PRUEBAS - Llave Maestra

| N.º | Caso de prueba | Entrada | Resultado esperado | Resultado obtenido |
|---:|---|---|---|---|
| 1 | Crear alquiler válido | Herramienta activa y fechas válidas | Se crea el alquiler y se registra la reserva | Pendiente de ejecución |
| 2 | Crear alquiler sin fecha inicial | Fecha inicial vacía | Se muestra error de fecha inicial obligatoria | Pendiente de ejecución |
| 3 | Crear alquiler sin fecha final | Fecha final vacía | Se muestra error de fecha final obligatoria | Pendiente de ejecución |
| 4 | Fechas invertidas | Fecha final anterior a fecha inicial | Se rechaza el formulario | Pendiente de ejecución |
| 5 | Fecha inicial pasada | Inicio anterior a hoy | Se rechaza el formulario | Pendiente de ejecución |
| 6 | Duración superior a 15 días | Rango de 16 días | Se muestra error del límite de 15 días | Pendiente de ejecución |
| 7 | Consultar alquileres propios | Usuario autenticado | Se muestran únicamente sus alquileres | Pendiente de ejecución |
| 8 | Usuario sin alquileres | Lista vacía | Se muestra el mensaje de lista vacía | Pendiente de ejecución |
| 9 | Solicitar cancelación | Alquiler reservado con inicio futuro | Aparece la confirmación sin enviar petición HTTP | Pendiente de ejecución |
| 10 | Confirmar cancelación | Pulsar confirmar | Se envía PATCH y se actualiza el estado del alquiler | Pendiente de ejecución |
| 11 | Usuario sin permiso de creación | Usuario sin ALQUILERES_CREAR | No aparece el botón Alquilar | Pendiente de ejecución |
| 12 | Acceso sin autenticación | Abrir ruta protegida sin sesión | Se bloquea el acceso mediante authGuard | Pendiente de ejecución |
