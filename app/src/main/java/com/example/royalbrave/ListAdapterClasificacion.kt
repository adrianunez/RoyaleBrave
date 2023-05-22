package com.example.royalbrave

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.royalbrave.modelos.Equipo
import com.squareup.picasso.Picasso

class ListAdapterClasificacion(val listaEquipos: ArrayList<Equipo>, val context: Context): RecyclerView.Adapter<ListAdapterClasificacion.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_clasificacion ,parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nombre = listaEquipos[position].nombre
        val points = listaEquipos[position].points
        val rank = listaEquipos[position].rank
        val logo = listaEquipos[position].logo

        holder.tvNombre.setText(nombre)
        holder.tvPosicion.setText(rank.toString())
        holder.tvPuntos.setText(points.toString())

        Picasso.get().load(logo).into(holder.imagenEquipo)
        }

    override fun getItemCount(): Int {
        return listaEquipos.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tvPosicion = view.findViewById<TextView>(R.id.textViewClasificacionPosicion)
        val tvNombre = view.findViewById<TextView>(R.id.textViewClasificacionNombre)
        val tvPuntos = view.findViewById<TextView>(R.id.textViewClasificacionPuntos)

        val imagenEquipo = view.findViewById<ImageView>(R.id.imageViewClasificacionEquipo)
    }

}