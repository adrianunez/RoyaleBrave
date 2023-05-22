package com.example.royalbrave

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.royalbrave.modelos.Jugador
import com.squareup.picasso.Picasso

class ListAdapterGoleadores(val listaGoleadores: ArrayList<Jugador>, val context: Context): RecyclerView.Adapter<ListAdapterGoleadores.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_goleadores ,parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val posicion = listaGoleadores[position].posicion
        val nombre = listaGoleadores[position].nombre
        val goles = listaGoleadores[position].goles
        val foto = listaGoleadores[position].foto
        val fotoEquipo = listaGoleadores[position].fotoEquipo

        holder.tvPosicion.setText(posicion.toString())
        holder.tvNombre.setText(nombre)
        holder.tvGoles.setText(goles.toString())

        Picasso.get().load(foto).into(holder.imageJugador)
        Picasso.get().load(fotoEquipo).into(holder.imageEquipoJugador)
        }

    override fun getItemCount(): Int {
        return listaGoleadores.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tvPosicion = view.findViewById<TextView>(R.id.textViewGoleadorPosicion)
        val tvNombre = view.findViewById<TextView>(R.id.textViewGoleadorNombre)
        val imageJugador = view.findViewById<ImageView>(R.id.imageViewGoleadorFoto)
        val imageEquipoJugador = view.findViewById<ImageView>(R.id.imageViewGoleadoresEquipo)
        val tvGoles = view.findViewById<TextView>(R.id.textViewGoleadorGoles)
    }
}