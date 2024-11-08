# Aplicación de Vacantes de Empleo

Esta es una aplicación web para la gestión de vacantes de empleo, desarrollada con **Spring Boot**, dockerizada y desplegada en **Render**, con una base de datos **MySQL** en **Clever Cloud**. Además, permite la gestión de archivos e imágenes, almacenados de forma segura en **Cloudinary**.

🌐 **URL de la Aplicación**: [https://proyempleos4.onrender.com](https://proyempleos4.onrender.com)
**testear con usuario ADMINISTRADOR** user:marisol pass:mari123

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
- Verificación de correos con un token único.
- Bloqueo de usuarios
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
2. **Cloudinary**: Añade tus credenciales de Cloudinary en el archivo .env (`cloud_name`, `api_key`, `api_secret`).
3. **Render**: Implementa el despliegue a Render si deseas replicar el entorno en vivo.

## Ejecución
Para ejecutar la aplicación en Docker:
```bash
docker build -t empleosapp .
docker run -p 8080:8080 empleosapp
```
## Algunas Capturas
![image](https://github.com/user-attachments/assets/6b08ab83-d28d-4fe7-bdb6-06c5a2ac9bc5)
![image](https://github.com/user-attachments/assets/09a12ea0-e954-4a5d-af51-7984606e0aa9)
![image](https://github.com/user-attachments/assets/ba08e2b3-3286-42c1-81d1-2a8eda5e4b7e)
![image](https://github.com/user-attachments/assets/14761651-b11b-4be3-89aa-3f9a21441309)
![image](https://github.com/user-attachments/assets/436ae0e1-8e19-49c1-b091-1941f9d5f5fa)





