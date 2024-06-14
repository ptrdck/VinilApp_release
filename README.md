# VinilApp

VinilApp es una aplicación móvil desarrollada como Proyecto de Integración para la aprobación del grado de Desarrollo de Aplicaciones Multiplataforma. La aplicación está diseñada para los seguidores del grupo musical "Los Vinilos" y ofrece funcionalidades como acceso a contenido exclusivo, solicitud de presupuestos para eventos y la capacidad de compartir fotos de conciertos.

## Características Principales

- **Autenticación**: Inicio de sesión y registro de usuarios utilizando Firebase Authentication, incluyendo la opción de iniciar sesión con Google.
- **Gestión de Datos**: Uso de Firebase Realtime Database para almacenar y gestionar datos de usuarios y contratos.
- **Almacenamiento de Imágenes**: Integración con Firebase Cloud Storage para almacenar imágenes de perfil y fotos de usuarios.
- **Personalización de Usuario**: Los usuarios pueden editar su perfil y subir fotos.
- **Formulario de Contratos**: Solicitud de presupuestos para eventos, con integración futura de Google Maps para la gestión de ubicaciones.
- **Historia y Recorrido de Vinilos**: Sección que muestra la historia y fotos de los vinilos.
- **Galería de Fotos de Usuarios**: Los usuarios pueden subir fotos mensuales tipo "selfies", con reconocimiento facial de los miembros del grupo Vinilos.

## Tecnologías Utilizadas

- **Jetpack Compose**: Para la creación de interfaces de usuario de forma declarativa.
- **Firebase**: Autenticación, Realtime Database y Cloud Storage.
- **Kotlin**: Lenguaje de programación principal utilizado.
- **MVVM (Model-View-ViewModel)**: Patrón de diseño utilizado para la arquitectura de la aplicación.
- **Glide**: Biblioteca para la carga eficiente de imágenes.

## Instalación y Configuración

1. Clona el repositorio:
    ```bash
    git clone https://github.com/ptrdck/VinilApp_v3.git
    cd VinilApp_v3
    ```

2. Abre el proyecto en Android Studio.

3. Configura Firebase:
    - Agrega el archivo `google-services.json` en el directorio `app/`.
    - Asegúrate de que Firebase Authentication, Realtime Database y Cloud Storage estén configurados en tu proyecto de Firebase.

4. Sincroniza el proyecto con Gradle y compila la aplicación.

## Uso de la Aplicación

- **Inicio de Sesión**: Los usuarios pueden registrarse o iniciar sesión utilizando su correo electrónico o su cuenta de Google.
- **Perfil de Usuario**: Los usuarios pueden editar su perfil, cambiar su imagen de perfil y ver sus datos personales.
- **Solicitud de Contratos**: Los usuarios pueden llenar un formulario para solicitar presupuestos para eventos.
- **Historia de Vinilos**: Los usuarios pueden explorar la historia y fotos del grupo "Los Vinilos".
- **Galería de Fotos**: Los usuarios pueden subir fotos mensuales y ver las fotos de otros usuarios.

## Contribución

¡Las contribuciones son bienvenidas! Para contribuir, sigue estos pasos:

1. Haz un fork del repositorio.
2. Crea una nueva rama (`git checkout -b feature/nueva-funcionalidad`).
3. Realiza los cambios necesarios y realiza commits (`git commit -m 'Añadir nueva funcionalidad'`).
4. Empuja los cambios a tu repositorio (`git push origin feature/nueva-funcionalidad`).
5. Abre un Pull Request para revisar los cambios.

## Licencia (por estudiar mejor, versión previa sujeta a estudio y cambios)

Este proyecto está bajo la licencia MIT. Ver el archivo [LICENSE](LICENSE) para más detalles.

## Contacto

Para más información o preguntas sobre el proyecto, puedes contactar a:

- **Pedro Patricio Cárdenas Figueroa**
- Email: [pedropatriciomus@gmail.com](mailto:pedropatriciomus@gmail.com)
- Email: [piterduck@gmail.com](mailto:piterduck@gmail.com)

---

Este README proporciona una visión general clara y concisa del proyecto VinilApp, sus características principales, tecnologías utilizadas, instrucciones de instalación y configuración, así como información sobre cómo contribuir al proyecto. Si necesitas más ajustes o detalles adicionales, por favor házmelo saber.
