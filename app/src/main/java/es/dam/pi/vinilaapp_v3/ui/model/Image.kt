package es.dam.pi.vinilaapp_v3.ui.model

data class Image(
    val imageId: String,
    val galleryId: String,
    val imageUrl: String
){
    // Este constructor vacío es necesario para Firebase
    constructor() : this("", "", "")
}