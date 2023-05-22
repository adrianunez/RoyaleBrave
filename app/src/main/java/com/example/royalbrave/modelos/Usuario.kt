package com.example.royalbrave.modelos

import android.graphics.Bitmap
import android.media.Image
import android.provider.ContactsContract
import java.io.Serializable

class Usuario(
    var id: Int,
    var nombre: String,
    var apellido: String,
    var genero: String,
    var fecha_nacimiento: String,
    var email: String,
    var monedero: Float,
    var es_admin: Boolean
) : Serializable {
    var fotoPerfil: ByteArray? = null
        private set

    fun setFotoPerfil(foto: ByteArray) {
        fotoPerfil = foto
    }
}
