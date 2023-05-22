package com.example.royalbrave

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import com.example.royalbrave.modelos.Usuario
import java.io.ByteArrayOutputStream

class PerfilFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var tvMonedero = view.findViewById<TextView>(R.id.textViewMonedero)
        var tvTransacciones = view.findViewById<TextView>(R.id.textViewEstadisticasGlobales)
        var tvCerrarSesion = view.findViewById<TextView>(R.id.textViewCerrarSesión)

        var imagenAtras = view.findViewById<ImageView>(R.id.imageViewFlechaAtrasPerfil)
        var imagenPerfil = view.findViewById<ImageView>(R.id.circularImageView)

        var tvNombre = view.findViewById<TextView>(R.id.textViewPerfilNombre)

        var bttnEditar = view.findViewById<TextView>(R.id.buttonEditarPerfil)

        val user = arguments?.getSerializable("usuario") as? Usuario

        if (user != null){
            tvNombre.setText(user.nombre)
        }

        var imageFotoPerfil = byteArrayToBitmap(user!!.fotoPerfil!!)
        imagenPerfil.setImageBitmap(imageFotoPerfil)

        imagenAtras.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("usuario", user)
            findNavController().navigate(R.id.action_perfilFragment_to_PPConLogFragment, bundle)
        }

        tvCerrarSesion.setOnClickListener {
            findNavController().navigate(R.id.action_perfilFragment_to_PPSinLogFragment)
        }
        tvTransacciones.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("usuario", user)
            findNavController().navigate(R.id.action_perfilFragment_to_estadisticasFragment, bundle)
        }
        tvMonedero.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("usuario", user)
            findNavController().navigate(R.id.action_perfilFragment_to_monederoFragment, bundle)
        }

        bttnEditar.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("usuario", user)
            findNavController().navigate(R.id.action_perfilFragment_to_editarPerfilFragment, bundle)
        }
    }
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}