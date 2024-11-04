# Aplicación de Vacantes de Empleo

Esta es una aplicación web para la gestión de vacantes de empleo, desarrollada con **Spring Boot**, dockerizada y desplegada en **Render**, con una base de datos **MySQL** en **Clever Cloud**. Además, permite la gestión de archivos e imágenes, almacenados de forma segura en **Cloudinary**.

🌐 **URL de la Aplicación**: [https://proyempleos4.onrender.com](https://proyempleos4.onrender.com)

## Tecnologías Utilizadas
- **Spring Boot** - Framework principal v3.1.1 para la creación de la aplicación.
- **Docker** - Contenerización de la aplicación para un despliegue sencillo y eficiente.
- **Render** - Plataforma de despliegue para ejecutar la aplicación en un entorno en vivo.
- **Clever Cloud** - Proveedor de base de datos MySQL.
- **Cloudinary** - Almacenamiento externo de imágenes y archivos para una gestión eficaz y segura.

## Funcionalidades
- Publicación y gestión de vacantes de empleo.
- Búsqueda de empleos por categoría y palabras clave.
- Envío de correos.
- **Almacenamiento de Archivos**: Los usuarios pueden subir imágenes asociadas a vacantes y almacenar CVs en formato PDF y Word de manera segura en **Cloudinary**.
- **Spring Security** para gestionar la autenticación y autorización de usuarios.
- Paginación en las vistas.
- CRUD de vacantes.
- CRUD de usuarios.
- CRUD de categorias.
- CRUD de solicitudes.

## Configuración
Para ejecutar el proyecto localmente, asegúrate de configurar los siguientes detalles en tu archivo de configuración:
1. **Base de datos**: Configura tu acceso a MySQL.
2. **Cloudinary**: Añade tus credenciales de Cloudinary (`cloud_name`, `api_key`, `api_secret`).
3. **Render**: Implementa el despliegue a Render si deseas replicar el entorno en vivo.

## Ejecución
Para ejecutar la aplicación en Docker:
```bash
docker build -t empleosapp .
docker run -p 8080:8080 empleosapp
