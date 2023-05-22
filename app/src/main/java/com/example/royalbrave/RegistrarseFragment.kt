package com.example.royalbrave

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.royalbrave.modelos.Usuario
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class RegistrarseFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registrarse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var btnnRegistrarse = view.findViewById<Button>(R.id.buttonRegistrarse)
        var calendario = view.findViewById<ImageView>(R.id.imageViewCalendario)

        var nombreEditText = view.findViewById<EditText>(R.id.nombreEditText)
        var apellidosEditText = view.findViewById<EditText>(R.id.apellidosEditText2)
        var emailEditText = view.findViewById<EditText>(R.id.emailEditText2)
        var fechaEditText = view.findViewById<EditText>(R.id.fechaEditText)
        var contrasenaEditText = view.findViewById<EditText>(R.id.contrasenaEditText)
        var hombreRadioButton= view.findViewById<RadioButton>(R.id.hombreRadioButton)
        var mujerRadioButton= view.findViewById<RadioButton>(R.id.mujerRadioButton)

        val validacionContrasena = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")
        val validacionLetras = Regex("[a-zA-Z]+")

        btnnRegistrarse.setOnClickListener {
            if (nombreEditText.text.isNotEmpty() && apellidosEditText.text.isNotEmpty() && (emailEditText.text.isNotEmpty() && validarEmail(emailEditText.text.toString())) && (contrasenaEditText.text.isNotEmpty() && validacionContrasena.matches(contrasenaEditText.text.toString())) && (fechaEditText.text.isNotEmpty() && validarMayorEdad(fechaEditText)) && (hombreRadioButton.isChecked || mujerRadioButton.isChecked))
            {
                var url = "http://royalbrave.es/api/v1/user/registrarse"

                val jsonObject = JSONObject()

                jsonObject.put("nombre", nombreEditText.text)
                jsonObject.put("apellido", apellidosEditText.text)
                if (hombreRadioButton.isChecked){
                    jsonObject.put("genero", "M")
                }else if (mujerRadioButton.isChecked){
                    jsonObject.put("genero", "F")
                }
                jsonObject.put("fecha_nacimiento", fechaEditText.text)
                jsonObject.put("email", emailEditText.text)
                jsonObject.put("monedero", 0)
                jsonObject.put("contrase_a", contrasenaEditText.text)

                val requestQueue = Volley.newRequestQueue(context)
                val jsonObjectRequest = object : JsonObjectRequest(
                    Method.POST, url, jsonObject,
                    Response.Listener { response ->
                        findNavController().navigate(R.id.action_registrarseFragment_to_iniciarSesionFragment)
                    },
                    Response.ErrorListener { error ->
                        Log.i("------>", error.toString())
                    }) {
                }
                requestQueue.add(jsonObjectRequest)
            }else{
                showAlert("Todos los campos son requeridos")
                if(nombreEditText.text.isEmpty()){
                    nombreEditText.setError("Campo requerido")
                }
                if (nombreEditText.text.length > 30){
                    nombreEditText.setError("Nombre demasiado largo")
                }
                if (nombreEditText.text.length < 3){
                    nombreEditText.setError("Nombre demasiado corto")
                }
                if(apellidosEditText.text.isEmpty()){
                    apellidosEditText.setError("Campo requerido")
                }
                if (nombreEditText.text.length > 30){
                    nombreEditText.setError("Apellido demasiado largo")
                }
                if (nombreEditText.text.length < 2){
                    nombreEditText.setError("Apellido demasiado corto")
                }
                if(emailEditText.text.isEmpty()){
                    emailEditText.setError("Campo requerido")
                }
                if (!validarEmail(emailEditText.text.toString())) {
                    emailEditText.setError("Email no valido")
                }
                if(contrasenaEditText.text.isEmpty()) {
                    contrasenaEditText.setError("Campo requerido")
                }
                if (fechaEditText.text.isNotEmpty()) {
                    if (!validarMayorEdad(fechaEditText)) {
                        fechaEditText.setError("El usuario tiene que ser mayor de edad")
                    }
                } else {
                    fechaEditText.setError("Campo requerido")
                }
                if(!hombreRadioButton.isChecked && !mujerRadioButton.isChecked) {
                    showAlert("Se debe de introducir un genero al usuario")
                }
                if (nombreEditText.text.isNotEmpty() && validacionLetras.matches(nombreEditText.text.toString())) {
                    // El nombre solo tiene letras
                } else {
                    nombreEditText.setError("El nombre solo puede contener letras")
                }
                if (apellidosEditText.text.isNotEmpty() && validacionLetras.matches(apellidosEditText.text.toString())) {
                    // El apellido solo tiene letras
                } else {
                    apellidosEditText.setError("El apellido solo puede contener letras")
                }
                if (contrasenaEditText.text.isNotEmpty() && validacionContrasena.matches(contrasenaEditText.text.toString())) {
                    // La contraseña esta bien
                } else {
                    contrasenaEditText.setError("La contraseña debe contener al menos una letra mayúscula, una letra minúscula, un número y un carácter especial (@$!%*?&) y tener al menos 8 caracteres")
                }
            }
        }

        calendario.setOnClickListener {
            mostrarDatePickerDialog()
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
    private fun mostrarDatePickerDialog() {
        val datePicker = DatePickerFragment { dia, mes, año -> onDateSelected(dia, mes + 1, año) }
        datePicker.show(parentFragmentManager, "datePicker")
    }

    private fun onDateSelected(dia: Int, mes: Int, año: Int) {
        val fechaEditText = view?.findViewById<EditText>(R.id.fechaEditText)
        val mesStr = if (mes < 10) "0$mes" else "$mes"
        val diaStr = if (dia < 10) "0$dia" else "$dia"
        fechaEditText?.setText("$dia-$mesStr-$año")
    }
    private fun validarMayorEdad(fechaEditText: EditText): Boolean {
        val fechaNacimiento = fechaEditText.text.toString()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fecha = sdf.parse(fechaNacimiento)
        val fechaActual = Calendar.getInstance().time
        val edad = fechaActual.year - fecha.year
        if (fecha.after(fechaActual)) {
            return false // la fecha de nacimiento es posterior a la fecha actual
        } else if (edad < 18) {
            return false //usuario no es mayor de edad
        }
        return true
    }

}