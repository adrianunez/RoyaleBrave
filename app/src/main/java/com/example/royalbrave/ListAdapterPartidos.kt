package com.example.royalbrave

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.royalbrave.modelos.Partido
import com.example.royalbrave.modelos.Usuario
import com.squareup.picasso.Picasso


class ListAdapterPartidos(val listaPartidos: ArrayList<Partido>, val user: Usuario, val navController: NavController, val context: Context): RecyclerView.Adapter<ListAdapterPartidos.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_partidos ,parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val logoLocal = listaPartidos[position].local.logo
        val logoVisitante = listaPartidos[position].visitante.logo
        val cuotaLocal = listaPartidos[position].cuotaLocal
        val cuotaVisitante = listaPartidos[position].cuotaVisitante
        val cuotaEmpate = listaPartidos[position].cuotaEmpate

        Picasso.get().load(logoLocal).into(holder.imagenLocal)
        Picasso.get().load(logoVisitante).into(holder.imagenVisitante)


        holder.cuotaLocal.setText(cuotaLocal.toString())
        holder.cuotaVisitante.setText(cuotaVisitante.toString())
        holder.cuotaEmpate.setText(cuotaEmpate.toString())

        holder.cuotaLocal.setOnClickListener {
            val bottomSheet = BottomSheetFragment(listaPartidos[position], true)
            val bundle = Bundle()
            bundle.putSerializable("partido", listaPartidos[position])
            bundle.putSerializable("usuario", user)
            bottomSheet.arguments = bundle
            bottomSheet.show((context as FragmentActivity).supportFragmentManager, "BottomSheetDialog")
        }

        holder.cuotaVisitante.setOnClickListener {
            val bottomSheet = BottomSheetFragment(listaPartidos[position], false)
            val bundle = Bundle()
            bundle.putSerializable("partido", listaPartidos[position])
            bundle.putSerializable("usuario", user)
            bottomSheet.arguments = bundle
            bottomSheet.show((context as FragmentActivity).supportFragmentManager, "BottomSheetDialog")
        }

        holder.cuotaEmpate.setOnClickListener{
            val bottomSheet = BottomSheetFragmentEmpate(listaPartidos[position], false)
            val bundle = Bundle()
            bundle.putSerializable("partido", listaPartidos[position])
            bundle.putSerializable("usuario", user)
            bottomSheet.arguments = bundle
            bottomSheet.show((context as FragmentActivity).supportFragmentManager, "BottomSheetDialog")
        }

        holder.imagenLocal.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("partido", listaPartidos[position])
            bundle.putSerializable("usuario", user)
            navController.navigate(R.id.action_PPConLogFragment_to_detallesPartidoFragment, bundle)
        }
        holder.imagenVisitante.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("partido", listaPartidos[position])
            bundle.putSerializable("usuario", user)
            navController.navigate(R.id.action_PPConLogFragment_to_detallesPartidoFragment, bundle)
        }

        holder.lLayout.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("partido", listaPartidos[position])
            bundle.putSerializable("usuario", user)
            navController.navigate(R.id.action_PPConLogFragment_to_detallesPartidoFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return listaPartidos.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imagenLocal = view.findViewById<ImageView>(R.id.imageViewLocal)
        val imagenVisitante = view.findViewById<ImageView>(R.id.imageViewVisitante)

        val cuotaLocal = view.findViewById<TextView>(R.id.textViewCuotaLocal)
        val cuotaVisitante = view.findViewById<TextView>(R.id.textViewCuotaVisitante)
        val cuotaEmpate = view.findViewById<TextView>(R.id.textViewCuotaEmpate)

        var lLayout = view.findViewById<ConstraintLayout>(R.id.linearLayoutPartidos)
    }

}