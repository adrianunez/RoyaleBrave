package com.example.royalbrave

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.royalbrave.modelos.Usuario
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.regex.Pattern

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private val PREFS_NAME = "MyPrefs"
private val KEY_USERNAME = "username"

class IniciarSesionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_iniciar_sesion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var buttonEnviar = view.findViewById<Button>(R.id.buttonEnviar)
        var aquiRegistrate = view.findViewById<TextView>(R.id.textViewAquiRegistrate)

        var etEmailPersona = view.findViewById<EditText>(R.id.emailEditText2)
        var etContrasenaPersona = view.findViewById<EditText>(R.id.contrasenaEditText)

        buttonEnviar.setOnClickListener {
            if (etEmailPersona.text.isNotEmpty() && etContrasenaPersona.text.isNotEmpty()) {
                if (validarEmail(etEmailPersona.text.toString())) {
                    var url = "http://royalbrave.es/api/v1/user/logearse"

                    val jsonObject = JSONObject()
                    var user: Usuario? = null
                    val requestQueue = Volley.newRequestQueue(context)

                    jsonObject.put("email", etEmailPersona.text.toString())
                    jsonObject.put("contrase_a", etContrasenaPersona.text.toString())


                    val jsonObjectRequest = object : JsonObjectRequest(
                        Method.POST, url, jsonObject,
                        { response ->
                            val jsonUsuario = response.getJSONObject("user")
                            val id = jsonUsuario.getInt("id")
                            val nombre = jsonUsuario.getString("nombre")
                            val apellido = jsonUsuario.getString("apellido")
                            val genero = jsonUsuario.getString("genero")
                            val fecha_nacimiento_str = jsonUsuario.getString("fecha_nacimiento")
                            val fecha_nacimiento =
                                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(
                                    fecha_nacimiento_str
                                )
                            val email = jsonUsuario.getString("email")
                            val monedero = jsonUsuario.getInt("monedero").toFloat()
                            val esAdmin = jsonUsuario.getBoolean("es_admin")
                            user = Usuario(
                                id,
                                nombre,
                                apellido,
                                genero,
                                fecha_nacimiento.toString(),
                                email,
                                monedero,
                                esAdmin
                            )

                            if (user!!.es_admin) {
                                findNavController().navigate(R.id.action_iniciarSesionFragment_to_panelAdminFragment2)
                            } else {
                                val requestQueueImagen = Volley.newRequestQueue(context)

                                val imageUrl =
                                    "http://10.59.135.44:3000/api/v1/user/get-profile-picture/" + user!!.id

                                val imageRequest = ImageRequest(
                                    imageUrl,
                                    { response ->
                                        var byteArrayFoto = bitmapToByteArray(response)
                                        user!!.setFotoPerfil(byteArrayFoto)
                                        val bundle = Bundle()
                                        bundle.putSerializable("usuario", user)
                                        findNavController().navigate(
                                            R.id.action_iniciarSesionFragment_to_PPConLogFragment,
                                            bundle
                                        )
                                    }, 0, 0, null, null
                                )
                                requestQueueImagen.add(imageRequest)
                            }
                        },
                        { error ->
                            Log.i("----->", error.toString())
                            showAlert("Ha habido un error al autentificar el usuario")
                        }) {

                    }
                    requestQueue.add(jsonObjectRequest)
                } else {
                    etEmailPersona.setError("Email no valido")
                }
            } else {
                etEmailPersona.setError("Campo Requerido")
                etContrasenaPersona.setError("Campo Requerido")
                Toast.makeText(context, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
        aquiRegistrate.setOnClickListener {
            findNavController().navigate(R.id.action_iniciarSesionFragment_to_registrarseFragment)
        }


    }

    private fun validarEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialog)
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}