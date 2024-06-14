package es.dam.pi.vinilaapp_v3.ui.model

data class User(
    val userId: String,
    val username: String,
    val email: String,
    val profilePictureUrl: String? = null,
    val galleryId: String? = null
)