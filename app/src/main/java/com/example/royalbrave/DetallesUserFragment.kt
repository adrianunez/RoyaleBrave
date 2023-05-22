package com.example.royalbrave

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.royalbrave.modelos.Usuario

class DetallesUserFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalles_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var bttnEliminar = view.findViewById<Button>(R.id.buttonEliminarDetalles)
        var bttnEditar = view.findViewById<Button>(R.id.buttonEditarDetalles)

        var nombre = view.findViewById<TextView>(R.id.textViewNombreDetalle)
        var apellido = view.findViewById<TextView>(R.id.textViewApellidoDetalles)
        var genero = view.findViewById<TextView>(R.id.textViewGeneroDetalle)
        var fecha = view.findViewById<TextView>(R.id.textViewFechaDetalle)
        var correo = view.findViewById<TextView>(R.id.textViewCorreoDetalle)
        var dinero = view.findViewById<TextView>(R.id.textViewDineroDetalle)

        var fotoPerfil = view.findViewById<ImageView>(R.id.imageViewPanelDetallesFoto)

        val usuario = arguments?.getSerializable("usuario") as? Usuario

        if (user != null){
            nombre.setText(usuario!!.nombre)
            apellido.setText(usuario!!.apellido)
            genero.setText(usuario!!.genero)

            var imageFotoPerfil = byteArrayToBitmap(user!!.fotoPerfil!!)
            fotoPerfil.setImageBitmap(imageFotoPerfil)

            val nuevaFecha = usuario!!.fecha_nacimiento.substring(0, usuario!!.fecha_nacimiento.length - 14)
            fecha.setText(nuevaFecha)
            correo.setText(usuario!!.email)
            dinero.setText(usuario!!.monedero.toString())
        }

        bttnEliminar.setOnClickListener{
            val queue = Volley.newRequestQueue(context)
            val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialog)
            builder.setTitle(getString(R.string.detalles_titulo_error) + " " + usuario!!.nombre + " " + usuario!!.apellido)
            builder.setMessage(getString(R.string.detalles_contenido_error) + " "+ usuario!!.nombre + " " + usuario!!.apellido + "?")
            builder.setPositiveButton(getString(R.string.detalles_alerta_si)) { dialog, which ->
                var url = "http://royalbrave.es/api/v1/user/delete/" + usuario!!.id

                val stringRequest = StringRequest(
                    Request.Method.DELETE, url,
                    { response ->
                        findNavController().navigate(R.id.action_detallesUserFragment_to_panelAdminFragment2)
                    },
                    { error ->
                        Log.i("--ERROOOOOR-->", "Usuario NO eliminado")
                    })
                queue.add(stringRequest)
            }
            builder.setNegativeButton(getString(R.string.detalles_alerta_no)) { dialog, which ->
                // Si se deja en blanco vuelve para atras a la pantalla
            }
            val dialog = builder.create()
            dialog.show()
        }
        bttnEditar.setOnClickListener{
            val bundle = Bundle()
            bundle.putSerializable("usuario", user)
            findNavController().navigate(R.id.action_detallesUserFragment_to_actualizarUserFragment, bundle)
        }
    }
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}