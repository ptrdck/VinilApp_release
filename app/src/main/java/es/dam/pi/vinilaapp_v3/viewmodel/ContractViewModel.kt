package es.dam.pi.vinilaapp_v3.viewmodel

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import es.dam.pi.vinilaapp_v3.data.ContractDatabaseManager
import es.dam.pi.vinilaapp_v3.ui.model.Contract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class ContractViewModel(
    private val contractDatabaseManager: ContractDatabaseManager,
    private val context: Context
) : ViewModel() {

    private val _contract = MutableStateFlow(
        Contract(
            userId = "",
            name = FirebaseAuth.getInstance().currentUser?.displayName ?: "",
            email = FirebaseAuth.getInstance().currentUser?.email ?: "",
            phone = "",
            concertType = "privado",
            duration = "2h",
            description = "",
            address = "",
            concertDate = "",
            latitude = null,
            longitude = null
        )
    )
    val contract: StateFlow<Contract> = _contract

    private val _isFormValid = MutableStateFlow(false)
    val isFormValid: StateFlow<Boolean> = _isFormValid

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    private val _successState = MutableStateFlow<String?>(null)
    val successState: StateFlow<String?> = _successState

    init {
        validateForm() // Validar el formulario al iniciar
    }

    fun updateField(
        name: String? = null,
        email: String? = null,
        phone: String? = null,
        concertType: String? = null,
        duration: String? = null,
        description: String? = null,
        concertDate: String? = null,
        address: String? = null,
        latitude: Double? = null,
        longitude: Double? = null
    ) {
        val updatedContract = _contract.value.copy(
            name = name ?: _contract.value.name,
            email = email ?: _contract.value.email,
            phone = phone ?: _contract.value.phone,
            concertType = concertType ?: _contract.value.concertType,
            duration = duration ?: _contract.value.duration,
            description = description ?: _contract.value.description,
            concertDate = concertDate ?: _contract.value.concertDate,
            address = address ?: _contract.value.address,
            latitude = latitude ?: _contract.value.latitude,
            longitude = longitude ?: _contract.value.longitude
        )
        _contract.value = updatedContract
        validateForm()
    }

    private val _contracts = MutableStateFlow<List<Contract>>(emptyList())
    val contracts: StateFlow<List<Contract>> = _contracts


    fun loadContracts(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val contracts = contractDatabaseManager.getContractsByUserId(userId)
                withContext(Dispatchers.Main) {
                    _contracts.value = contracts
                }
            } catch (e: Exception) {
                Log.e("ContractViewModel", "Failed to load user contracts", e)
            }
        }
    }

    private fun validateForm() {
        val contract = _contract.value
        _isFormValid.value = contract.name.isNotBlank() &&
                contract.email.isNotBlank() &&
                Patterns.EMAIL_ADDRESS.matcher(contract.email).matches() &&
                contract.phone.isNotBlank() &&
                Patterns.PHONE.matcher(contract.phone).matches() &&
                contract.concertType.isNotBlank() &&
                contract.duration.isNotBlank() &&
                contract.address.isNotBlank() &&
                contract.concertDate.isNotBlank() &&
                contract.description.isNotBlank() &&
                contract.description.length <= 200
    }

    fun createContract() {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                val contractWithUserId = _contract.value.copy(userId = userId)
                try {
                    val isCreated = withContext(Dispatchers.IO) {
                        contractDatabaseManager.createContract(contractWithUserId)
                    }
                    if (isCreated) {
                        sendEmailToAdmin(contractWithUserId)
                        _successState.value = "Contrato creado exitosamente y correo enviado."
                        _errorState.value = null
                    } else {
                        _errorState.value = "Ya tienes 3 contratos creados."
                        _successState.value = null
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _errorState.value = "Error al crear el contrato: ${e.message}"
                    _successState.value = null
                }
            } else {
                _errorState.value = "Usuario no autenticado."
                _successState.value = null
            }
        }
    }

    private suspend fun sendEmailToAdmin(contract: Contract) {
        withContext(Dispatchers.IO) {
            val adminEmail = "piterduck@gmail.com"
            val userEmail = contract.email
            val subject = "Nuevo contrato generado"

            val message = """
                Buenas tardes,
                
                El usuario ${contract.name} ha generado la siguiente propuesta de concierto:
                
                - **Nombre**: ${contract.name}
                - **Correo**: ${contract.email}
                - **Teléfono**: ${contract.phone}
                - **Tipo de Concierto**: ${contract.concertType}
                - **Duración**: ${contract.duration}
                - **Fecha del Concierto**: ${contract.concertDate}
                - **Dirección**: ${contract.address}
                - **Latitud**: ${contract.latitude ?: "No proporcionada"}
                - **Longitud**: ${contract.longitude ?: "No proporcionada"}
                - **Descripción**: ${contract.description}
                
                Nos comunicaremos vía email y/o teléfono para comunicarle el presupuesto para su solicitud. Muchas gracias.
            """.trimIndent()

            val properties = Properties().apply {
                put("mail.smtp.auth", "true")
                put("mail.smtp.starttls.enable", "true")
                put("mail.smtp.host", "smtp.gmail.com")
                put("mail.smtp.port", "587")
                put("mail.smtp.ssl.trust", "smtp.gmail.com")
            }

            val session = Session.getInstance(properties, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication("piterduck@gmail.com", "skzd atwn cikw xcer")
                }
            })

            try {
                val mimeMessage = MimeMessage(session).apply {
                    setFrom(InternetAddress("piterduck@gmail.com"))
                    setRecipients(Message.RecipientType.TO, InternetAddress.parse(adminEmail))
                    setSubject(subject)
                    setText(message)
                }

                Transport.send(mimeMessage)
            } catch (e: MessagingException) {
                e.printStackTrace()
                throw e
            }
        }
    }

    fun clearMessages() {
        _errorState.value = null
        _successState.value = null
    }
}
