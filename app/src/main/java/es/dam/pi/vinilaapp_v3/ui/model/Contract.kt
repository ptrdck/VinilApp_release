package es.dam.pi.vinilaapp_v3.ui.model

import java.util.UUID

data class Contract(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val concertType: String = "privado",
    val duration: String = "2h",
    val description: String = "",
    val address: String = "",
    val concertDate: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val status: String = "Solicitado"
) {
    // Constructor sin argumentos necesario para Firebase
    constructor() : this(
        id = UUID.randomUUID().toString(),
        userId = "",
        name = "",
        email = "",
        phone = "",
        concertType = "privado",
        duration = "2h",
        description = "",
        address = "",
        concertDate = "",
        latitude = null,
        longitude = null,
        status = "Solicitado"
    )
}
