package com.example.royalbrave

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.royalbrave.modelos.Usuario

class ListAdapterPanelAdmin(
    val listaUsuarios: ArrayList<Usuario>,
    val context: Context,
    val navController: NavController
) : RecyclerView.Adapter<ListAdapterPanelAdmin.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nombre = listaUsuarios[position].nombre
        val email = listaUsuarios[position].email
        val id = listaUsuarios[position].id
        holder.tvItem.text = nombre
        holder.tvItem2.text = email
        holder.tvItem3.text = id.toString()

        holder.bttnEliminar.setOnClickListener {
            val queue = Volley.newRequestQueue(context)
            val builder = AlertDialog.Builder(context, R.style.AlertDialog)
            builder.setTitle(context.getString(R.string.detalles_titulo_error) + " " + listaUsuarios[position].nombre + " " + listaUsuarios[position].apellido)
            builder.setMessage(context.getString(R.string.detalles_contenido_error) + " " + listaUsuarios[position].nombre + " " + listaUsuarios[position].apellido + "?")
            builder.setPositiveButton(context.getString(R.string.detalles_alerta_si)) { dialog, which ->
                var url =
                    "http://royalbrave.es/api/v1/user/delete/" + listaUsuarios[position].id

                val stringRequest = StringRequest(
                    Request.Method.DELETE, url,
                    { response ->
                        listaUsuarios.removeAt(position)
                        notifyDataSetChanged()
                    },
                    { error ->
                        Log.i("--ERROOOOOR-->", "Usuario NO eliminado")
                    })
                queue.add(stringRequest)
            }
            builder.setNegativeButton(context.getString(R.string.detalles_alerta_no)) { dialog, which ->
                // Volver a la pantalla normal si se selecciona "No"
            }
            val dialog = builder.create()
            dialog.show()
        }

        holder.bttnEditar.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("usuario", user)
            navController.navigate(R.id.action_panelAdminFragment2_to_actualizarUserFragment, bundle)
        }

        holder.tvItem.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("usuario", user)
            navController.navigate(R.id.action_panelAdminFragment2_to_detallesUserFragment, bundle)
        }
        holder.tvItem2.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("usuario", user)
            navController.navigate(R.id.action_panelAdminFragment2_to_detallesUserFragment, bundle)
        }

    }

    override fun getItemCount(): Int {
        return listaUsuarios.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvItem = view.findViewById<TextView>(R.id.tv_itemNombre)
        val tvItem2 = view.findViewById<TextView>(R.id.tv_itemEmail)
        val tvItem3 = view.findViewById<TextView>(R.id.tv_itemId)
        val bttnEliminar = view.findViewById<Button>(R.id.buttonEliminarLista)
        val bttnEditar = view.findViewById<Button>(R.id.buttonEditarLista)
    }

}