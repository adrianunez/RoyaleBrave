package com.example.royalbrave

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.royalbrave.modelos.Apuesta
import com.example.royalbrave.modelos.Partido
import com.example.royalbrave.modelos.Usuario
import com.squareup.picasso.Picasso
class DetallesPartidoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalles_partido, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val partido = arguments?.getSerializable("partido") as? Partido
        val usuario = arguments?.getSerializable("usuario") as? Usuario

        //Definimos las apuestas que puede realizar el usuario para que esten en todos los idiomas
        val apuestaResultadoExacto = Apuesta(getString(R.string.apuesta_resultado), listOf(getString(R.string.apuesta_local) + " " + partido!!.cuotaLocal.toString(), getString(R.string.apuesta_empatan) + " " + partido.cuotaEmpate.toString(), getString(R.string.apuesta_visitante) + " " + partido!!.cuotaVisitante))
        val apuestaGoles = Apuesta(getString(R.string.apuesta_goles), listOf(getString(R.string.apuesta_mas) + " " + partido!!.cuotaMasGoles.toString(), getString(R.string.apuesta_menos)+ " " + partido!!.cuotaMenosGoles.toString()))
        val apuestaTarjetasAmarillas = Apuesta(getString(R.string.apuesta_amarillas), listOf(getString(R.string.apuesta_mas) + " " + partido!!.cuotaMasAmarillas.toString(), getString(R.string.apuesta_menos)+ " " + partido!!.cuotaMenosAmarillas.toString()))
        val apuestaTarjetasRojas = Apuesta(getString(R.string.apuesta_rojas), listOf(getString(R.string.apuesta_mas) + " " + partido!!.cuotaMasRojas.toString(), getString(R.string.apuesta_menos)+ " " + partido!!.cuotaMenosRojas.toString()))
        val apuestaCorners = Apuesta(getString(R.string.apuesta_corners), listOf(getString(R.string.apuesta_mas) + " " + partido!!.cuotaMasCorners.toString(), getString(R.string.apuesta_menos)+ " " + partido!!.cuotaMenosCorners.toString()))
        val apuestaTirosAPuerta = Apuesta(getString(R.string.apuesta_tiros), listOf(getString(R.string.apuesta_mas) + " " + partido!!.cuotaMasChutes.toString(), getString(R.string.apuesta_menos)+ " " + partido!!.cuotaMenosChutes.toString()))

        val apuestas:ArrayList<Apuesta> = arrayListOf(apuestaResultadoExacto, apuestaGoles,
            apuestaTarjetasAmarillas, apuestaTarjetasRojas, apuestaCorners, apuestaTirosAPuerta)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewApuestas)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ApuestaAdapter(apuestas, partido, usuario!!, requireContext())

        val logoLocal = partido!!.local.logo
        val logoVisitante = partido!!.visitante.logo

        val imagenLocal = view.findViewById<ImageView>(R.id.imageViewDetallesLocal)
        val imagenVisitante = view.findViewById<ImageView>(R.id.imageViewDetallesVisitante)
        val imagenLiga = view.findViewById<ImageView>(R.id.imageViewLiga)

        val tvNombreLocal = view.findViewById<TextView>(R.id.textViewDetallesLocal)
        val tvNombreVisitante = view.findViewById<TextView>(R.id.textViewDetallesVisitante)
        val tvFechaPartido = view.findViewById<TextView>(R.id.textViewDetallesFecha)
        val tvHoraPartido = view.findViewById<TextView>(R.id.textViewDetallesHora)
        val tvCampoPartido = view.findViewById<TextView>(R.id.textViewDetallesCampo)
        val tvLiga = view.findViewById<TextView>(R.id.textViewDetallesNombreLiga)
        val tvFechaHora = view.findViewById<TextView>(R.id.textViewDetallesFechaHora)

        Picasso.get().load(logoLocal).into(imagenLocal)
        Picasso.get().load(logoVisitante).into(imagenVisitante)

        tvNombreLocal.setText(partido.local.nombre)
        tvNombreVisitante.setText(partido.visitante.nombre)

        tvFechaPartido.setText(partido!!.fecha)
        tvHoraPartido.setText(partido!!.hora)

        Picasso.get().load(partido!!.logoLiga).into(imagenLiga)
        tvLiga.setText(partido!!.nombreLiga)
        tvFechaHora.setText(partido!!.fecha + " " + getString(R.string.detalles_partido_fecha_tiempo) + " " + partido!!.hora)
        tvCampoPartido.setText(partido!!.campo)

    }
}