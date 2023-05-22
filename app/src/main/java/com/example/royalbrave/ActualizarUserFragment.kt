package com.example.royalbrave

import android.app.AlertDialog
import android.content.Intent
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [ActualizarUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ActualizarUserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Usuario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as Usuario?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_actualizar_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var bttnRegistrarse = view.findViewById<Button>(R.id.buttonRegistrarse)
        var calendario = view.findViewById<ImageView>(R.id.imageViewCalendario)

        var nombreEditText = view.findViewById<EditText>(R.id.nombreEditText)
        var apellidosEditText = view.findViewById<EditText>(R.id.apellidosEditText2)
        var emailEditText = view.findViewById<EditText>(R.id.emailEditText2)
        var fechaEditText = view.findViewById<EditText>(R.id.fechaEditText)
        var hombreRadioButton= view.findViewById<RadioButton>(R.id.hombreRadioButton)
        var mujerRadioButton= view.findViewById<RadioButton>(R.id.mujerRadioButton)
        var checkAdmin = view.findViewById<CheckBox>(R.id.checkBoxAdmin)

        val validacionLetras = Regex("[a-zA-Z]+")

        nombreEditText.setText(param1!!.nombre)
        apellidosEditText.setText(param1!!.apellido)
        emailEditText.setText(param1!!.email)

        val nuevaFecha = param1!!.fecha_nacimiento.substring(0, param1!!.fecha_nacimiento.length - 14)
        fechaEditText.setText(nuevaFecha)
        if (param1!!.genero == "M"){
            hombreRadioButton.isChecked = true
            mujerRadioButton.isChecked = false
        }
        if (param1!!.genero == "F"){
            mujerRadioButton.isChecked = true
            hombreRadioButton.isChecked = false
        }
        checkAdmin.isChecked = param1!!.es_admin

        calendario.setOnClickListener {
            mostrarDatePickerDialog()
        }

        bttnRegistrarse.setOnClickListener{
            if (nombreEditText.text.isNotEmpty() && apellidosEditText.text.isNotEmpty() && (emailEditText.text.isNotEmpty() && validarEmail(emailEditText.text.toString())) &&  (fechaEditText.text.isNotEmpty() && validarMayorEdad(fechaEditText)) && (hombreRadioButton.isChecked || mujerRadioButton.isChecked))
            {
                var url = "http://royalbrave.es/api/v1/user/update/" + param1!!.id

                val jsonObject = JSONObject()

                jsonObject.put("nombre", nombreEditText.text)
                jsonObject.put("apellido", apellidosEditText.text)
                if (hombreRadioButton.isChecked){
                    jsonObject.put("genero", "M")
                }else if (mujerRadioButton.isChecked){
                    jsonObject.put("genero", "F")
                }
                jsonObject.put("fecha_nacimiento", fechaEditText.text.toString() + "T15:30:00.000Z")
                jsonObject.put("email", emailEditText.text)
                jsonObject.put("monedero", param1!!.monedero)
                if (checkAdmin.isChecked){
                    jsonObject.put("es_admin",true)
                }else {
                    jsonObject.put("es_admin", false)
                }

                val requestQueue = Volley.newRequestQueue(context)
                val jsonObjectRequest = object : JsonObjectRequest(
                    Method.PUT, url, jsonObject,
                    Response.Listener { response ->
                        findNavController().navigate(R.id.action_actualizarUserFragment_to_panelAdminFragment2)
                    },
                    Response.ErrorListener { error ->
                        Log.i("------>", error.toString())
                        Log.i("------>", jsonObject.toString())
                    }) {
                }
                requestQueue.add(jsonObjectRequest)
            }else{
                showAlert("Todos los campos son requeridos")
                if(nombreEditText.text.isEmpty()){
                    nombreEditText.setError("Campo requerido")
                }
                if(apellidosEditText.text.isEmpty()){
                    apellidosEditText.setError("Campo requerido")
                }
                if(emailEditText.text.isEmpty()){
                    emailEditText.setError("Campo requerido")
                }
                if (!validarEmail(emailEditText.text.toString())) {
                    emailEditText.setError("Email no valido")
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
                    // El nombre solo contiene letras
                } else {
                    nombreEditText.setError("El nombre solo puede contener letras")
                }
                if (apellidosEditText.text.isNotEmpty() && validacionLetras.matches(apellidosEditText.text.toString())) {
                    // El apellido solo contiene letras
                } else {
                    apellidosEditText.setError("El apellido solo puede contener letras")
                }
            }
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
        fechaEditText?.setText("$diaStr-$mesStr-$año")
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
            return false // el usuario no es mayor de edad
        }
        return true
    }
}