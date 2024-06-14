### VinilApp - Documentación Funcional

## Descripción del Proyecto

VinilApp es una aplicación móvil desarrollada como Proyecto de Integración para la aprobación del grado de Desarrollo de Aplicaciones Multiplataforma. La aplicación está diseñada para los seguidores del grupo musical "Los Vinilos" y ofrece funcionalidades como acceso a contenido exclusivo, solicitud de presupuestos para eventos y la capacidad de compartir fotos de conciertos. Además, la aplicación facilita la interacción con la banda, permite la personalización de perfiles de usuario y ofrece información detallada sobre la banda y sus eventos.

## Características Principales

### Autenticación

- **Inicio de Sesión y Registro**: Utilizando **Firebase Authentication**, los usuarios pueden registrarse y iniciar sesión utilizando su correo electrónico y contraseña. También se incluye la opción de iniciar sesión mediante Google Sign-In.
- **Verificación de Correo Electrónico**: Para garantizar la seguridad, se requiere que los usuarios verifiquen su correo electrónico antes de poder acceder a todas las funcionalidades de la aplicación.
- **Recuperación de Contraseña**: Los usuarios pueden recuperar sus contraseñas a través de un enlace enviado a su correo electrónico.

### Gestión de Datos

- **Almacenamiento de Datos de Usuarios**: Utilizando **Firebase Realtime Database**, se gestionan y sincronizan en tiempo real los datos de los usuarios y los detalles de los contratos.
- **Consulta y Modificación de Datos**: Los usuarios pueden consultar y modificar sus datos personales directamente desde la aplicación.

### Almacenamiento de Imágenes

- **Imágenes de Perfil y Fotos**: Integración con **Firebase Cloud Storage** para almacenar de manera segura las imágenes de perfil de los usuarios y las fotos que suben.
- **Carga y Visualización de Imágenes**: Utilización de la biblioteca **Glide** para cargar y mostrar imágenes de manera eficiente en la aplicación.

### Personalización de Usuario

- **Edición de Perfil**: Los usuarios pueden editar su perfil, incluyendo su nombre, correo electrónico y foto de perfil.
- **Visualización de Datos Personales**: Los usuarios pueden ver sus datos personales y realizar cambios cuando lo deseen.

### Formulario de Contratos

- **Solicitud de Presupuestos**: Los usuarios pueden llenar un formulario detallado para solicitar presupuestos para eventos. El formulario incluye campos para la dirección, tipo de concierto, duración, fecha del concierto y detalles de contacto.
- **Gestión de Solicitudes**: Las solicitudes de presupuesto se almacenan en **Firebase Realtime Database** y se gestionan en tiempo real.

### Historia y Recorrido de Vinilos

- **Sección de Historia**: Proporciona información detallada sobre la historia del grupo "Los Vinilos", incluyendo fotos y eventos pasados.
- **Carga de Imágenes**: Las imágenes se pueden cargar desde diferentes fuentes, incluyendo almacenamiento local, **Firebase Cloud Storage** y la web.

### Galería de Fotos de Usuarios

- **Subida de Fotos**: Los usuarios pueden subir fotos mensuales tipo "selfies" que se almacenan en **Firebase Cloud Storage**.
- **Reconocimiento Facial**: Utilización de la API de **Google Cloud Image** para el reconocimiento facial de los miembros del grupo Vinilos en las fotos subidas por los usuarios.

### Integraciones Externas

- **Google Maps**: Integración para mostrar ubicaciones en los eventos utilizando Google Maps.
- **Visor de YouTube**: Permite reproducir videos de YouTube directamente en la aplicación para promover eventos y compartir contenido multimedia.
- **Glide**: Biblioteca para la carga y visualización eficiente de imágenes en la aplicación.

## Requisitos del Sistema

- **Android**: Dispositivo con Android 9.0 (Pie) o superior.
- **Conexión a Internet**: Necesaria para la autenticación, sincronización de datos y acceso a contenidos en la nube.
- **Espacio de Almacenamiento**: Al menos 50 MB de espacio libre para la instalación y almacenamiento de datos temporales.

## Instalación y Configuración

1. **Descarga e Instalación**:
   - Descarga la aplicación desde [Google Play Store](#).

2. **Configuración Inicial**:
   - Al iniciar la aplicación por primera vez, el usuario debe registrarse o iniciar sesión utilizando su correo electrónico o su cuenta de Google.

3. **Permisos Necesarios**:
   - La aplicación solicitará permisos para acceder a la cámara, almacenamiento y ubicación (en caso de integrar Google Maps).

## Uso de la Aplicación

### Inicio de Sesión

- **Registro**: Los nuevos usuarios pueden registrarse utilizando su correo electrónico y contraseña. Se les pedirá que verifiquen su correo electrónico antes de continuar.
- **Inicio de Sesión**: Los usuarios registrados pueden iniciar sesión utilizando sus credenciales de correo electrónico o su cuenta de Google.
- **Recuperación de Contraseña**: Los usuarios que olviden su contraseña pueden utilizar la función de recuperación para recibir un enlace de restablecimiento en su correo electrónico.

### Perfil de Usuario

- **Edición de Perfil**: Los usuarios pueden editar su perfil para actualizar su nombre, correo electrónico y foto de perfil.
- **Visualización de Datos Personales**: Los usuarios pueden consultar sus datos personales y realizar cambios según sea necesario.

### Solicitud de Contratos

- **Formulario de Solicitud**: Los usuarios pueden llenar un formulario detallado para solicitar presupuestos para eventos. El formulario incluye campos para la dirección, tipo de concierto, duración, fecha del concierto y detalles de contacto.
- **Envío y Confirmación**: Una vez completado el formulario, los usuarios pueden enviar su solicitud para obtener un presupuesto. Se proporciona una confirmación del envío exitoso.

### Historia de Vinilos

- **Exploración de Contenido**: Los usuarios pueden explorar la historia del grupo "Los Vinilos", incluyendo información sobre la banda, eventos pasados y fotos.
- **Carga de Imágenes**: Las imágenes se pueden cargar desde almacenamiento local, **Firebase Cloud Storage** y la web, proporcionando una experiencia rica en contenido visual.

### Galería de Fotos

- **Subida de Fotos**: Los usuarios pueden subir fotos mensuales tipo "selfies" que se almacenan en **Firebase Cloud Storage**.
- **Reconocimiento Facial**: Utilización de la API de **Google Cloud Image** para el reconocimiento facial de los miembros del grupo Vinilos en las fotos subidas por los usuarios.

## Gestión de Estados en Jetpack Compose

### Manejo de Estados

- **State Management**: Se utilizan `State`, `LiveData`, `StateFlow`, y `MutableStateFlow` para gestionar los estados de la UI. Los cambios en el estado desencadenan automáticamente la recomposición de la UI, manteniendo la interfaz actualizada con los datos más recientes.

### Ejemplos de Código

**Uso de `remember` y `mutableStateOf`**:

```kotlin
@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Otros componentes de UI

    TextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("Email") },
        singleLine = true
    )

    TextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Password") },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation()
    )

    Button(onClick = {
        viewModel.signInWithEmailAndPassword(email, password)
    }) {
        Text("Login")
    }
}
```

**Uso de `LiveData` con `observeAsState`**:

```kotlin
@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavController) {
    val loginSuccess by viewModel.loginSuccess.observeAsState(false)
    val context = LocalContext.current

    if (loginSuccess) {
        context.startActivity(Intent(context, HomeActivity::class.java))
    }

    // Otros componentes de UI
}
```

**Uso de `StateFlow` y `MutableStateFlow` en ViewModel**:

```kotlin
class LoginViewModel(
    private val googleAuthUiClient: GoogleAuthUiClient,
    private val userViewModel: UserViewModel
) : ViewModel() {

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    fun signInWithEmailAndPassword(email: String, password: String, context: Context) = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        if (user.isEmailVerified) {
                            userViewModel.loadUser(user.uid, user)
                            _loginSuccess.value = true
                        } else {
                            Toast.makeText(context, "Por favor, verifica tu correo electrónico.", Toast.LENGTH_LONG).show()
                            auth.signOut()
                        }
                    } else {
                        Toast.makeText(context, "Error al iniciar sesión: Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Error al iniciar sesión: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (ex: Exception) {
            Toast.makeText(context, "Error al iniciar sesión: ${ex.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
```

**Uso de `rememberSaveable` para mantener el estado a través de recomposiciones**:

```kotlin
@Composable
fun SignInScreen(viewModel: LoginViewModel, navController: NavController) {
    var

 email by rememberSaveable { mutableStateOf(viewModel.email.value ?: "") }
    var password by rememberSaveable { mutableStateOf(viewModel.password.value ?: "") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    Column {
        // Otros componentes de UI
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        Button(onClick = {
            if (password == confirmPassword) {
                viewModel.createUserWithEmailAndPassword(email, password, context)
            } else {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Sign Up")
        }
    }
}
```

## Integraciones Externas

### Firebase Authentication

- **Descripción**: Proporciona servicios de autenticación backend fáciles de usar que se integran con las aplicaciones móviles.
- **Implementación**:
  ```gradle
  implementation 'com.google.firebase:firebase-auth-ktx'
  ```
  ```kotlin
  FirebaseApp.initializeApp(this)
  ```

### Firebase Realtime Database

- **Descripción**: Permite almacenar y sincronizar datos entre usuarios en tiempo real.
- **Implementación**:
  ```gradle
  implementation 'com.google.firebase:firebase-database-ktx'
  ```
  ```kotlin
  FirebaseApp.initializeApp(this)
  ```

### Firebase Cloud Storage

- **Descripción**: Proporciona una solución escalable para almacenar archivos, incluyendo imágenes y videos.
- **Implementación**:
  ```gradle
  implementation 'com.google.firebase:firebase-storage-ktx'
  ```
  ```kotlin
  FirebaseApp.initializeApp(this)
  ```

### Google Maps

- **Descripción**: Utilizado para mostrar ubicaciones en la aplicación.
- **Implementación**:
  ```gradle
  implementation 'com.google.android.gms:play-services-maps'
  ```
  ```xml
  <meta-data
      android:name="com.google.android.geo.API_KEY"
      android:value="@string/google_maps_key"/>
  ```

### Visor de YouTube

- **Descripción**: Permite mostrar videos directamente en la aplicación.
- **Implementación**:
  ```gradle
  implementation 'com.google.android.youtube:youtube-android-player-api'
  ```

### Glide

- **Descripción**: Biblioteca de carga de imágenes eficiente para Android.
- **Implementación**:
  ```gradle
  implementation 'com.github.bumptech.glide:glide:4.12.0'
  annotationProcessor 'com.github.bumptech.glide:compiler:4.12.0'
  ```

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
