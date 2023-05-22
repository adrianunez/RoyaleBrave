package com.example.royalbrave.modelos

import java.io.Serializable

class Equipo(
    val id: Int,
    val nombre: String,
    val logo: String,
    val rank: Int = 0,
    val points: Int = 0,
    val es_local: Boolean
) : Serializable {
    constructor(id: Int, nombre: String, logo: String) : this(id, nombre, logo, 0, 0, false)
}
