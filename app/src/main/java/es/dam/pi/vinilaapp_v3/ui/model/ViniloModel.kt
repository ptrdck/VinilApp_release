package es.dam.pi.vinilaapp_v3.ui.model

import androidx.annotation.DrawableRes

data class ViniloModel(
    val nombre: String,
    val descripcion: String,
    @DrawableRes val foto: Int,
    val web: String,
    val biografia: String // Nueva propiedad
)
