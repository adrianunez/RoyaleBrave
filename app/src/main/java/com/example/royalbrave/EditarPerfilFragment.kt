package com.example.royalbrave

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.royalbrave.modelos.Usuario
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

val PICK_IMAGE_REQUEST = 1
var user :Usuario? = null
val BOUNDARY = UUID.randomUUID().toString()
private lateinit var imagePerfil: ImageView

class EditarPerfilFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editar_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = arguments?.getSerializable("usuario") as? Usuario

        val validacionLetras = Regex("[a-zA-Z]+")

        var etNombre = view.findViewById<EditText>(R.id.editTextEditarNombre)
        var etApellido = view.findViewById<EditText>(R.id.editTextEditarApellido)

        var tvCorreo = view.findViewById<TextView>(R.id.textViewEditarCorreo)
        var tvFecha = view.findViewById<TextView>(R.id.textViewFechaNacimientoPerfil)
        var tvGenero = view.findViewById<TextView>(R.id.textViewPerfilGenero)

        var imageGenero = view.findViewById<ImageView>(R.id.imageViewPerfilGenero)
        imagePerfil = view.findViewById(R.id.imageViewEditarPerfil)
        

        var bttnEditar = view.findViewById<Button>(R.id.buttonPerfilEditar)
        var bttnCambiarFoto = view.findViewById<Button>(R.id.buttonEditarFotoPerfil)

        etNombre.setText(user!!.nombre)
        etApellido.setText(user!!.apellido)
        var fotoPerfilBitmap = byteArrayToBitmap(user!!.fotoPerfil!!)
        imagePerfil.setImageBitmap(fotoPerfilBitmap)
        tvCorreo.setText(user!!.email)

        val fecha = SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(user!!.fecha_nacimiento)
        val fechaFormateada = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(fecha)
        val fechaConBarras = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha)

        tvFecha.setText(fechaConBarras)

        if (user!!.genero == "M"){
            tvGenero.setText(R.string.registrar_hombre)
        }else if (user!!.genero == "F"){
            tvGenero.setText(R.string.registrar_mujer)
        }


        if (user!!.genero == "M"){
            imageGenero.setImageResource(R.drawable.baseline_male_24)
            imageGenero.setColorFilter(R.color.azul)
        }else if (user!!.genero == "F"){
            imageGenero.setImageResource(R.drawable.baseline_female_24)
            imageGenero.setColorFilter(R.color.rosa)
        }

        bttnCambiarFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        bttnEditar.setOnClickListener {
            //Peticion para actualizar el usuario
            var url = "http://royalbrave.es/api/v1/user/update/" + user!!.id

            val jsonObject = JSONObject()

            if (etNombre.text.isEmpty()){
                jsonObject.put("nombre", user!!.nombre)
            }else if (!validacionLetras.matches(etNombre.text)){
                showAlert("El nombre debe contener solo letras")
            }else if (etNombre.text.length > 30){
                etNombre.setError("Nombre demasiado largo")
            } else if (etNombre.text.length < 3){
                etNombre.setError("Nombre demasiado corto")
            }else{
                jsonObject.put("nombre", etNombre.text.toString())
                user!!.nombre = etNombre.text.toString()
            }

            if (etApellido.text.isEmpty()){
                jsonObject.put("apellido", user!!.apellido)
            }else if (!validacionLetras.matches(etApellido.text)){
            showAlert("El nombre debe contener solo letras")
            }else if (etApellido.text.length > 30){
                etApellido.setError("Nombre demasiado largo")
            } else if (etApellido.text.length < 3){
                etApellido.setError("Nombre demasiado corto")
            }else{
                jsonObject.put("apellido", etApellido.text.toString())
                user!!.apellido = etApellido.text.toString()
            }

            jsonObject.put("genero", user!!.genero)

            jsonObject.put("fecha_nacimiento", fechaFormateada + "T15:30:00.000Z")
            jsonObject.put("email", user!!.email)
            jsonObject.put("monedero", user!!.monedero)
            jsonObject.put("es_admin",false)

            val requestQueue = Volley.newRequestQueue(context)
            val jsonObjectRequest = object : JsonObjectRequest(
                Method.PUT, url, jsonObject,
                Response.Listener { response ->
                    showAlert("El usuario ha sido cambiado correctamente")
                    val bundle = Bundle()
                    bundle.putSerializable("usuario", user)
                    findNavController().navigate(R.id.action_editarPerfilFragment_to_perfilFragment, bundle)
                },
                Response.ErrorListener { error ->
                    Log.i("------>", error.toString())
                }) {
            }
            requestQueue.add(jsonObjectRequest)
        }
    }
    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialog)
        builder.setTitle("Exito!")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
            imagePerfil.setImageBitmap(bitmap)
            editarFotoPerfil(user!!.id, bitmap)
            var nuevaFoto = bitmapToByteArray(bitmap)
            user!!.setFotoPerfil(nuevaFoto)
        }
    }
    fun editarFotoPerfil(id: Int, bitmap: Bitmap) {
        val url = "http://royalbrave.es/api/v1/user/update-profile-picture/$id"

        val requestQueue = Volley.newRequestQueue(context)
        val volleyMultipartRequest = object : JsonObjectRequest(
            Method.POST, url, null,
            Response.Listener { response ->
                showAlert("La foto de perfil ha sido actualizada correctamente")
            },
            Response.ErrorListener { error: VolleyError? ->
                Log.i("ERROR", error.toString())
            }) {
            //Se anula el método getHeaders() para que no se agreguen encabezados adicionales.
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                return headers
            }

            override fun getBodyContentType(): String {
                return "multipart/form-data; boundary=$BOUNDARY"
            }
            //Se anula el método getBody() para proporcionar el contenido real del cuerpo de la solicitud, donde se se construye el cuerpo multipart/form-data.
            @Throws(IOException::class)
            override fun getBody(): ByteArray {
                //Variables para escribir el cuerpo de la solicitud
                val bos = ByteArrayOutputStream()
                val dos = DataOutputStream(bos)

                dos.writeBytes("--$BOUNDARY\r\n")
                dos.writeBytes("Content-Disposition: form-data; name=\"profilePicture\"; filename=\"profile.png\"\r\n")
                dos.writeBytes("Content-Type: image/png\r\n\r\n")

                //Pasar a bytes
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                val imageBytes = outputStream.toByteArray()

                dos.write(imageBytes)
                dos.writeBytes("\r\n")
                dos.writeBytes("--$BOUNDARY--\r\n")
                dos.flush()
                dos.close()

                return bos.toByteArray()
            }

            override fun parseNetworkResponse(response: NetworkResponse): Response<JSONObject> {
                try {
                    val jsonString = String(response.data, Charset.defaultCharset())
                    return Response.success(JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: Exception) {
                    return Response.error(ParseError(e))
                }
            }
        }
        requestQueue.add(volleyMultipartRequest)
    }
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}