# Llave Maestra

## Qué es

Llave Maestra es una aplicación web para la gestión de alquiler de herramientas. Permite consultar un catálogo de herramientas, realizar y administrar alquileres, gestionar clientes y consultar reportes del negocio.

El sistema cuenta con dos tipos de usuarios: administradores y clientes. Cada uno tiene acceso a funcionalidades según su rol y sus permisos.

## Tecnologías

**Backend**
- Java.
- Spring Boot.
- Spring Security.
- Autenticación mediante JWT.
- Spring Data JPA.
- PostgreSQL.
- Swagger / OpenAPI.

**Frontend**
- Angular.
- TypeScript.
- HTML y CSS.
- RxJS.
- Angular Router.
- Interceptores HTTP y guards de autenticación y permisos.

## Cómo levantarlo

### Requisitos previos

- Java y Maven.
- Node.js y npm.
- PostgreSQL.

### 1.Crear la base de datos

    En PostgreSQL, ejecutar:

        CREATE DATABASE llave_db;

### 2. Iniciar el backend

Desde una terminal, entrar a la carpeta `api`:

```bash
cd api
```

Ejecutar:

```bash
./mvnw spring-boot:run
```

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Si el proyecto no incluye Maven Wrapper, se puede utilizar:

```bash
mvn spring-boot:run
```

### 3. Iniciar el frontend

Abrir otra terminal y entrar a la carpeta `web`:

```bash
cd web
npm install
npx ng serve
```

Abrir en el navegador:

http://localhost:4200

### 4. Documentación de la API

Con el backend iniciado, consultar Swagger UI en:

http://localhost:8080/swagger-ui/index.html

## Variables de entorno

El backend utiliza variables de entorno para configurar la conexión a la base de datos y la clave de firma de los tokens JWT.
 -------------------------------------------------------------------------------------------
|   Variable   | Descripción             | Valor predeterminado de desarrollo               |
|--------------|-----------------------  |--------------------------------------------------|
| `DB_USER`    | Usuario de PostgreSQL   | `postgres`                                       |
| `DB_PASSWORD`| Contraseña de PostgreSQL| `postgres`                                       |
| `JWT_SECRET` | Clave secreta para      |                                                  |
|              | firmar los tokens JWT   | `llave-maestra-clave-secreta-de-desarrollo-2026` |
---------------------------------------------------------------------------------------------

Los valores predeterminados son únicamente para desarrollo local. En un entorno real se deben configurar credenciales y secretos seguros.

La URL y el nombre de la base de datos se configuran en las propiedades del backend.

## Usuarios de prueba

Contraseña de los usuarios de demostración: `llave123`.
------------------------------------------------------
| Usuario|        Correo        |         Rol        |
|--------|--------------------  |--------------------|
| Hernán | admin@llavemaestra.co| ADMIN              |
| Pedro  | pedro@correo.com     | CLIENTE            |
| Diana  | diana@correo.com     | CLIENTE            |
| Jorge  | jorge@correo.com     | CLIENTE (inactivo) |
------------------------------------------------------

Estos usuarios permiten probar los diferentes permisos y comportamientos del sistema.

## Roles y permisos

### Administrador (ADMIN)

Tiene los siguientes permisos:

- HERRAMIENTAS_VER: consultar el catálogo de herramientas.
- HERRAMIENTAS_GESTIONAR: crear, editar y gestionar el estado de las herramientas.
- ALQUILERES_VER_TODOS: consultar todos los alquileres.
- ALQUILERES_GESTIONAR: gestionar los alquileres.
- CLIENTES_GESTIONAR: administrar los clientes.
- REPORTES_VER: consultar los reportes del negocio.

### Cliente (CLIENTE)

Tiene los siguientes permisos:

- HERRAMIENTAS_VER: consultar las herramientas disponibles.
- ALQUILERES_CREAR: crear alquileres.
- ALQUILERES_VER_PROPIOS: consultar sus propios alquileres.
- ALQUILERES_CANCELAR_PROPIOS: cancelar sus propios alquileres cuando las reglas del sistema lo permiten.

El backend valida los permisos y el frontend adapta las opciones disponibles según el usuario autenticado.

## Decisiones técnicas

- Se utiliza autenticación JWT para mantener una sesión sin estado en el backend.
- Spring Security protege los endpoints y controla el acceso mediante roles y permisos.
- Las reglas de negocio se implementan en la capa de servicios para separar responsabilidades.
- Se utiliza PostgreSQL para almacenar usuarios, herramientas y alquileres.
- Angular utiliza rutas con carga diferida para optimizar la carga de las páginas.
- Los interceptores HTTP gestionan el envío del token de autenticación.
- Los guards protegen las rutas según la sesión y los permisos del usuario.
- Se utilizan servicios Angular para centralizar la comunicación con la API.
- Swagger permite consultar y probar los endpoints del backend.

## Demo

**Video de demostración:** https://drive.google.com/file/d/1mL0BaFkFVZxut7tBbEiy5XPzYhsSXiGB/view?usp=sharing

