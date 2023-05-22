package com.example.royalbrave

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.royalbrave.modelos.Partido
import com.example.royalbrave.modelos.Transacción
import com.example.royalbrave.modelos.Usuario
import com.squareup.picasso.Picasso


class ListAdapterEstadisticasApuestas(val listaTransacciones: ArrayList<Transacción>, val navController: NavController, val context: Context): RecyclerView.Adapter<ListAdapterEstadisticasApuestas.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_ultimas_apuestas ,parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val cantidad = listaTransacciones[position].cantidad
        val fecha = listaTransacciones[position].fecha_realizada
        val estado = listaTransacciones[position].status
        val deporte = listaTransacciones[position].deporte
        val esPagado = listaTransacciones[position].esPagado

        holder.tvMonedero.setText("-$cantidad€")
        holder.tvFecha.setText(fecha)

        holder.tvEstado.setText(estado)
        if (estado == "Abierta"){
            val colorAzulClaro = ContextCompat.getColor(holder.itemView.context, R.color.azulClaro)
            holder.tvEstado.setTextColor(colorAzulClaro)
        }else if (estado == "Cerrada"){
            val colorSalmon = ContextCompat.getColor(holder.itemView.context, R.color.salmon)
            holder.tvEstado.setTextColor(colorSalmon)
        }

        holder.tvDeporte.setText(deporte)

        val colorLightGreen = ContextCompat.getColor(holder.itemView.context, R.color.lightGreen)
        val colorLightRed = ContextCompat.getColor(holder.itemView.context, R.color.lightRed)

        if (esPagado) {
            holder.tvEsPagado.setText(R.string.si)
            holder.tvEsPagado.setTextColor(colorLightGreen)
        } else {
            holder.tvEsPagado.setText("No")
            holder.tvEsPagado.setTextColor(colorLightRed)
        }


    }

    override fun getItemCount(): Int {
        return listaTransacciones.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tvDeporte = view.findViewById<TextView>(R.id.textViewEstadisticasDeporte)
        val tvFecha =  view.findViewById<TextView>(R.id.textViewEstadisticasFecha)
        val tvMonedero =  view.findViewById<TextView>(R.id.textViewEstadisticasCantidad)
        val tvEstado =  view.findViewById<TextView>(R.id.textViewEstadisticasEstado)
        val tvEsPagado =  view.findViewById<TextView>(R.id.textViewEstadisticaPagado)
    }

}