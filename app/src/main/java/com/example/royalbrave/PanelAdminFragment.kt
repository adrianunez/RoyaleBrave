package com.example.royalbrave

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.royalbrave.modelos.Usuario
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class PanelAdminFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_panel_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bttnCrear = view.findViewById<Button>(R.id.buttonCrearAdmin)

        bttnCrear.setOnClickListener {
            findNavController().navigate(R.id.action_panelAdminFragment2_to_crearNuevoAdminFragment)
        }

        GlobalScope.launch {
            val queue = Volley.newRequestQueue(requireContext())
            val usuariosArray: ArrayList<Usuario> = ArrayList()

            val url = "http://royalbrave.es/api/v1/user/getAll"
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    val jsonArray = response.getJSONArray("users")

                    for (i in 0 until jsonArray.length()) {
                        val userObject = jsonArray.getJSONObject(i)
                        var id = userObject.getInt("id")
                        var nombre = userObject.getString("nombre")
                        var apellido = userObject.getString("apellido")
                        var genero = userObject.getString("genero")
                        var fechaNacimiento = userObject.getString("fecha_nacimiento")
                        var email = userObject.getString("email")
                        var monedero = userObject.getInt("monedero").toFloat()
                        val user = Usuario(
                            id,
                            nombre,
                            apellido,
                            genero,
                            fechaNacimiento,
                            email,
                            monedero,
                            false
                        )
                        val requestQueueImagen = Volley.newRequestQueue(context)

                        val imageUrl = "http://royalbrave.es/api/v1/user/get-profile-picture/" + com.example.royalbrave.user!!.id

                        val imageRequest = ImageRequest(
                            imageUrl,
                            { response ->
                                var byteArrayFoto = bitmapToByteArray(response)
                                user!!.setFotoPerfil(byteArrayFoto)
                            }, 0, 0, null, null
                        )
                        requestQueueImagen.add(imageRequest)
                        usuariosArray.add(user)
                    }

                    MainScope().launch {
                        val rvList = view.findViewById<RecyclerView>(R.id.rv_list)
                        rvList.adapter = ListAdapterPanelAdmin(usuariosArray, requireActivity(), findNavController())
                        rvList.layoutManager = LinearLayoutManager(requireContext())
                        rvList.adapter?.notifyDataSetChanged()
                    }
                },
                { error ->
                    Log.i("--ERROR-->", error.toString())
                }
            )
            queue.add(jsonObjectRequest)
        }
    }
    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}