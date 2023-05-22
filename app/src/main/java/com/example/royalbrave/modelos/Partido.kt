package com.example.royalbrave.modelos

import android.os.Bundle
import java.io.Serializable

class Partido(var id:Int,
              var fecha: String,
              var hora: String,
              var campo: String,
              var local: Equipo,
              var visitante: Equipo,
              var logoLiga: String,
              var nombreLiga: String,
              var cuotaLocal: Float,
              var cuotaVisitante: Float,
              val cuotaEmpate: Float,
              val cuotaMasGoles: Float,
              val cuotaMenosGoles: Float,
              val cuotaMasAmarillas: Float,
              val cuotaMenosAmarillas: Float,
              val cuotaMasRojas: Float,
              val cuotaMenosRojas: Float,
              val cuotaMasCorners: Float,
              val cuotaMenosCorners: Float,
              val cuotaMasChutes: Float,
              val cuotaMenosChutes: Float

) : Serializable {
    fun getDatosPartido(): Bundle {
        val bundle = Bundle()
        bundle.putSerializable("partido", this)
        return bundle
    }
}

