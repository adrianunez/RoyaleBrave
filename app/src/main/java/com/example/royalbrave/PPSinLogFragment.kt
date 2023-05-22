package com.example.royalbrave

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


private lateinit var tvEspanol: TextView
private lateinit var tvCatalan: TextView
private lateinit var tvIngles: TextView

private lateinit var tvNosotrosPPSinLog: TextView
private lateinit var tvUbicacion: TextView
private lateinit var bttnIniciarSesion: Button
private lateinit var bttnRegistratse: Button

class PPSinLogFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_p_p_sin_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bttnIniciarSesion = view.findViewById(R.id.buttonIniciarSesionSinLog)
        bttnRegistratse = view.findViewById(R.id.buttonRegistrarseSinLog)
        tvUbicacion = view.findViewById(R.id.textViewUbicacion)


        bttnIniciarSesion.setOnClickListener {
            findNavController().navigate(R.id.action_PPSinLogFragment_to_iniciarSesionFragment)
        }
        bttnRegistratse.setOnClickListener {
            findNavController().navigate(R.id.action_PPSinLogFragment_to_registrarseFragment)
        }

        val mapFragment = SupportMapFragment.newInstance()
        val laSalleGracia = LatLng(41.398931, 2.150200)
        val zoomLevel = 15f

        childFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_mapa, mapFragment)
            .commit()

        mapFragment.getMapAsync { googleMap ->
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(laSalleGracia, zoomLevel))
            googleMap.addMarker(MarkerOptions().position(laSalleGracia).title("La Salle Gracia"))
        }

        tvNosotrosPPSinLog = view.findViewById(R.id.textViewNosotros)!!

        tvEspanol = view.findViewById(R.id.tvEspanol)!!
        var imEspanol = view.findViewById<ImageView>(R.id.imageViewEspanol)

        tvCatalan = view.findViewById(R.id.tvCatalan)!!
        var imCatalan = view.findViewById<ImageView>(R.id.imageViewCatalan)

        tvIngles = view.findViewById(R.id.tvIngles)
        var imIngles = view.findViewById<ImageView>(R.id.imageViewIngles)

        tvEspanol.setOnClickListener{
            actualizarResource("es")
            cambiarIdiomaIngles("es")
        }
        imEspanol!!.setOnClickListener{
            actualizarResource("es")
            cambiarIdiomaIngles("es")
        }

        tvCatalan.setOnClickListener{
            actualizarResource("ca")
            cambiarIdiomaIngles("ca")
        }
        imCatalan.setOnClickListener{
            actualizarResource("ca")
            cambiarIdiomaIngles("ca")
        }
        tvIngles.setOnClickListener{
            actualizarResource("en")
            cambiarIdiomaIngles("en")
        }
        imIngles.setOnClickListener{
            actualizarResource("en")
            cambiarIdiomaIngles("en")
        }

    }
    fun actualizarResource(idioma: String){
        val recursos = resources
        val displatMetrics = recursos.displayMetrics
        val configuracion = resources.configuration
        configuracion.setLocale(Locale(idioma))
        recursos.updateConfiguration(configuracion, displatMetrics)
        configuracion.locale = Locale(idioma)
        resources.updateConfiguration(configuracion, displatMetrics)

        tvNosotrosPPSinLog.text = recursos.getString(R.string.ppsinlog_nosotros)
        tvUbicacion.text = recursos.getString(R.string.ppsinlog_ubi)

        bttnIniciarSesion.setText(recursos.getString(R.string.ppsinlog_iniciarsesion))
        bttnRegistratse.setText(recursos.getString(R.string.ppsinlog_registrate))

        //tvBienvenido.text = recursos.getString(R.string.isesion_bienvenido)
    }
    private fun cambiarIdiomaIngles(idioma: String) {
        val locale = Locale(idioma)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, requireContext().resources.displayMetrics)
    }

}