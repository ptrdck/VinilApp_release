
# VinilApp - Documentación Técnica

## Descripción del Proyecto

VinilApp es una aplicación móvil desarrollada como Proyecto de Integración para la aprobación del grado de Desarrollo de Aplicaciones Multiplataforma. La aplicación está diseñada para los seguidores del grupo musical "Los Vinilos" y ofrece funcionalidades como acceso a contenido exclusivo, solicitud de presupuestos para eventos y la capacidad de compartir fotos de conciertos.

## Requisitos del Sistema

- **Android**: Dispositivo con Android 9.0 (Pie) o superior.
- **Conexión a Internet**: Necesaria para la autenticación y sincronización de datos con Firebase.
- **Espacio de Almacenamiento**: Al menos 50 MB de espacio libre para la instalación y almacenamiento de datos temporales.
- **Herramientas de Desarrollo**: Android Studio, Firebase, Kotlin.

## Arquitectura del Proyecto

### Patrón de Diseño

VinilApp utiliza el patrón de diseño **MVVM (Model-View-ViewModel)**, que ayuda a separar la lógica de negocio de la lógica de la interfaz de usuario. Este patrón facilita la mantenibilidad y escalabilidad del código.

### Estructura de Directorios

\`\`\`plaintext
VinilApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/vinilapp/
│   │   │   │   ├── model/
│   │   │   │   ├── view/
│   │   │   │   ├── viewmodel/
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   ├── drawable/
│   │   │   │   ├── values/
│   ├── androidTest/
│   ├── build.gradle
│   ├── AndroidManifest.xml
├── build.gradle
├── settings.gradle
\`\`\`

### Descripción de Directorios

- **model/**: Contiene las clases de datos y modelos utilizados en la aplicación.
- **view/**: Contiene las actividades y fragmentos que representan la interfaz de usuario.
- **viewmodel/**: Contiene las clases que actúan como intermediarios entre el modelo y la vista, manejando la lógica de negocio y la preparación de datos para la presentación.
- **layout/**: Archivos XML que definen el diseño de las interfaces de usuario.
- **drawable/**: Recursos gráficos utilizados en la aplicación.
- **values/**: Archivos XML que contienen valores reutilizables como cadenas, colores y estilos.

## Descripción del Código Fuente

### MainActivity.kt

\`\`\`kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VinilAppTheme {
                // Pantalla principal de la aplicación
                Navigation()
            }
        }
    }
}
\`\`\`

La \`MainActivity\` actúa como el punto de entrada de la aplicación, configurando la navegación y la interfaz inicial utilizando Jetpack Compose.

### ViewModel

#### LoginViewModel.kt

\`\`\`kotlin
class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    fun signInWithEmail(email: String, password: String) {
        // Lógica de autenticación
    }

    fun signInWithGoogle(idToken: String) {
        // Lógica de autenticación con Google
    }
}
\`\`\`

El \`LoginViewModel\` maneja la lógica de autenticación, utilizando Firebase Authentication para gestionar el inicio de sesión con correo electrónico y Google.

### Model

#### User.kt

\`\`\`kotlin
data class User(
    val userId: String,
    val username: String,
    val profilePictureUrl: String?
)
\`\`\`

La clase \`User\` representa los datos del usuario almacenados en Firebase Realtime Database.

### Vistas

#### LoginScreen.kt

\`\`\`kotlin
@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel = hiltViewModel()) {
    // UI de la pantalla de inicio de sesión
}
\`\`\`

La \`LoginScreen\` define la interfaz de usuario para el inicio de sesión, utilizando Jetpack Compose para la composición declarativa.

## Integración con Firebase

### Configuración de Firebase

1. **Agregar Firebase al Proyecto**:
   - En la consola de Firebase, agrega tu aplicación y descarga el archivo \`google-services.json\`.
   - Coloca el archivo \`google-services.json\` en el directorio \`app/\` del proyecto.

2. **Configuración del Gradle**:
   - Modifica los archivos \`build.gradle\` para incluir los servicios de Firebase.

\`\`\`groovy
// build.gradle (nivel del proyecto)
buildscript {
    dependencies {
        classpath 'com.google.gms:google-services:4.3.8'
    }
}

// build.gradle (nivel de la app)
apply plugin: 'com.android.application'
apply plugin: 'com.google.gms.google-services'

dependencies {
    implementation 'com.google.firebase:firebase-auth:21.0.1'
    implementation 'com.google.firebase:firebase-database:20.0.3'
    implementation 'com.google.firebase:firebase-storage:20.0.1'
}
\`\`\`

### Autenticación

#### Configuración de Firebase Authentication

1. **Habilitar Proveedores de Autenticación**:
   - En la consola de Firebase, ve a la sección de Authentication y habilita los proveedores de inicio de sesión que deseas utilizar (correo electrónico, Google, etc.).

2. **Implementación en la Aplicación**:
   - Usa FirebaseAuth para manejar el proceso de autenticación en la aplicación.

### Base de Datos en Tiempo Real

#### Configuración de Firebase Realtime Database

1. **Crear la Base de Datos**:
   - En la consola de Firebase, crea una nueva instancia de Firebase Realtime Database.

2. **Reglas de Seguridad**:
   - Configura las reglas de seguridad para controlar el acceso a la base de datos.

\`\`\`json
{
  "rules": {
    ".read": "auth != null",
    ".write": "auth != null"
  }
}
\`\`\`

3. **Implementación en la Aplicación**:
   - Usa FirebaseDatabase para interactuar con la base de datos desde la aplicación.

\`\`\`kotlin
val database = Firebase.database
val userRef = database.getReference("users")
\`\`\`

### Almacenamiento en la Nube

#### Configuración de Firebase Cloud Storage

1. **Crear un Bucket de Almacenamiento**:
   - En la consola de Firebase, crea un bucket de almacenamiento para tu aplicación.

2. **Reglas de Seguridad**:
   - Configura las reglas de seguridad para controlar el acceso al almacenamiento.

\`\`\`json
{
  "rules": {
    ".read": "auth != null",
    ".write": "auth != null"
  }
}
\`\`\`

3. **Implementación en la Aplicación**:
   - Usa FirebaseStorage para manejar la carga y descarga de archivos en la aplicación.

\`\`\`kotlin
val storage = Firebase.storage
val storageRef = storage.reference
val profileImagesRef = storageRef.child("profileImages")
\`\`\`

## Testing y Depuración

### Pruebas Unitarias

- Utiliza JUnit y Mockito para escribir y ejecutar pruebas unitarias de los ViewModel y otros componentes de la lógica de negocio.

### Pruebas de UI

- Utiliza Espresso para escribir y ejecutar pruebas de UI que interactúan con los componentes de la interfaz de usuario.

## Despliegue

### Preparación para el Despliegue

1. **Configuración de Firma para Publicación**:
   - Configura la firma de la aplicación en Android Studio para publicar en Google Play Store.

2. **Compilación y Empaquetado**:
   - Compila y empaqueta la aplicación para su despliegue utilizando las herramientas de Android Studio.

3. **Publicación en Google Play Store**:
   - Sigue los pasos de la consola de Google Play para subir y publicar tu aplicación.

## Mantenimiento y Actualizaciones

### Actualización de Dependencias

- Mantén actualizadas las dependencias del proyecto mediante la revisión y actualización periódica de los archivos \`build.gradle\`.

### Monitoreo de Errores

- Implementa Firebase Crashlytics para monitorear y registrar errores en la aplicación en tiempo real.

### Feedback de Usuarios

- Utiliza Firebase Analytics para recopilar datos de uso y feedback de los usuarios para mejorar continuamente la aplicación.

---

