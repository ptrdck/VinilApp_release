# VinilApp - Documentación Funcional

## Descripción del Proyecto

VinilApp es una aplicación móvil desarrollada como Proyecto de Integración para la aprobación del grado de Desarrollo de Aplicaciones Multiplataforma. La aplicación está diseñada para los seguidores del grupo musical "Los Vinilos" y ofrece funcionalidades como acceso a contenido exclusivo, solicitud de presupuestos para eventos y la capacidad de compartir fotos de conciertos.

## Características Principales

- **Autenticación**: 
  - Inicio de sesión y registro de usuarios utilizando Firebase Authentication, incluyendo la opción de iniciar sesión con Google.
  
- **Gestión de Datos**: 
  - Uso de Firebase Realtime Database para almacenar y gestionar datos de usuarios y contratos.

- **Almacenamiento de Imágenes**: 
  - Integración con Firebase Cloud Storage para almacenar imágenes de perfil y fotos de usuarios.

- **Personalización de Usuario**: 
  - Los usuarios pueden editar su perfil, cambiar su imagen de perfil y ver sus datos personales.

- **Formulario de Contratos**: 
  - Solicitud de presupuestos para eventos. Permite a los usuarios llenar un formulario con detalles del evento y enviar la solicitud para obtener un presupuesto.

- **Historia y Recorrido de Vinilos**: 
  - Sección que muestra la historia y fotos del grupo "Los Vinilos". Las imágenes se pueden cargar desde diferentes fuentes (local, Cloud Storage, web).

- **Galería de Fotos de Usuarios**: 
  - Los usuarios pueden subir fotos mensuales tipo "selfies", con reconocimiento facial de los miembros del grupo Vinilos. Las fotos se almacenan en Firebase Cloud Storage.

## Requisitos del Sistema

- **Android**: Dispositivo con Android 9.0 (Pie) o superior.
- **Conexión a Internet**: Necesaria para la autenticación y sincronización de datos con Firebase.
- **Espacio de Almacenamiento**: Al menos 50 MB de espacio libre para la instalación y almacenamiento de datos temporales.

## Instalación y Configuración

1. **Descarga e Instalación**:
   - Descarga la aplicación desde [Google Play Store](#).

2. **Configuración Inicial**:
   - Al iniciar la aplicación por primera vez, el usuario debe registrarse o iniciar sesión utilizando su correo electrónico o su cuenta de Google.

3. **Permisos Necesarios**:
   - La aplicación solicitará permisos para acceder a la cámara, almacenamiento y ubicación (en caso de integrar Google Maps).

## Uso de la Aplicación

1. **Inicio de Sesión**:
   - Los usuarios pueden registrarse o iniciar sesión utilizando su correo electrónico o su cuenta de Google.

2. **Perfil de Usuario**:
   - Los usuarios pueden editar su perfil, cambiar su imagen de perfil y ver sus datos personales.

3. **Solicitud de Contratos**:
   - Los usuarios pueden llenar un formulario para solicitar presupuestos para eventos, proporcionando detalles como la dirección, tipo de concierto, duración y contacto.

4. **Historia de Vinilos**:
   - Los usuarios pueden explorar la historia y fotos del grupo "Los Vinilos". La sección muestra información y fotos relevantes, cargadas de manera eficiente desde diferentes fuentes.

5. **Galería de Fotos**:
   - Los usuarios pueden subir fotos mensuales tipo "selfies", que se almacenan en Firebase Cloud Storage. La API de Cloud Image de Google se utiliza para el reconocimiento facial de los miembros del grupo Vinilos.

## Beneficios para el Usuario

- **Acceso Exclusivo**: Acceso a contenido exclusivo del grupo "Los Vinilos".
- **Fácil Solicitud de Contratos**: Proceso simplificado para solicitar presupuestos para eventos.
- **Personalización**: Posibilidad de personalizar el perfil y compartir fotos.
- **Interacción con el Grupo**: Funcionalidades que permiten a los usuarios interactuar y estar al día con las actividades del grupo.

## Preguntas Frecuentes (FAQ)

1. **¿Cómo puedo registrarme en VinilApp?**
   - Puedes registrarte utilizando tu correo electrónico o tu cuenta de Google.

2. **¿Cómo puedo solicitar un presupuesto para un evento?**
   - Llena el formulario de solicitud de contratos en la sección correspondiente y proporciona los detalles necesarios.

3. **¿Cómo puedo subir fotos a la galería?**
   - Accede a la sección de galería de fotos y sube tus fotos mensuales tipo "selfies".

4. **¿Es seguro almacenar mis datos en VinilApp?**
   - Sí, utilizamos Firebase para la autenticación y almacenamiento de datos, asegurando la seguridad y privacidad de tu información.

5. **¿Puedo editar mi perfil después de registrarme?**
   - Sí, puedes editar tu perfil y cambiar tu imagen de perfil en cualquier momento desde la sección de personalización de usuario.

## Contacto

Para más información o preguntas sobre la aplicación, puedes contactar a:

- Email: [pedropatriciomus@gmail.com](mailto:pedropatriciomus@gmail.com)
- Email: [piterduck@gmail.com](mailto:piterduck@gmail.com)


---
