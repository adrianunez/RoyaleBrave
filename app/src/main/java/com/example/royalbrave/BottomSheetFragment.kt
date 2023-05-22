package com.example.royalbrave

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.royalbrave.modelos.Partido
import com.example.royalbrave.modelos.Usuario
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import org.json.JSONObject

class BottomSheetFragment(private val partido: Partido, private val es_local: Boolean) :
    BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var tvNombre = view.findViewById<TextView>(R.id.textViewBottomNombre)
        var tvCuota = view.findViewById<TextView>(R.id.textViewBottomCuota)
        var imagenEscudo = view.findViewById<ImageView>(R.id.imageViewBotttomLogo)

        var etCantidad = view.findViewById<EditText>(R.id.editTextBottomCantidadApuesta)

        var buttonApuesta = view.findViewById<Button>(R.id.buttonHacerApuesta)

        val validacionLetras = Regex("[a-zA-Z]+")

        val usuario = arguments?.getSerializable("usuario") as Usuario

        if (es_local == true) {
            tvCuota.setText(partido.cuotaLocal.toString())
            tvNombre.setText(getString(R.string.bottom_gana) + " " + partido.local.nombre)
            Picasso.get().load(partido.local.logo).into(imagenEscudo)

            buttonApuesta.setOnClickListener {
                buttonApuesta.isEnabled = false

                val handler = Handler()
                handler.postDelayed({
                    buttonApuesta.isEnabled = true
                }, 5000)

                var url = "http://royalbrave.es/api/v1/futbol/guardarApuesta"

                val jsonObject = JSONObject()

                val requestQueue = Volley.newRequestQueue(context)

                jsonObject.put("deporte", "Futbol")
                jsonObject.put("tipo_apuesta", "Simple")
                jsonObject.put("cuota", partido.cuotaLocal)
                jsonObject.put("condicion", "local_gana")
                jsonObject.put("usuarioId", usuario.id)
                jsonObject.put("cantidad", etCantidad.text.toString().toFloat())
                jsonObject.put("partido_id", partido.id.toString())

                val jsonObjectRequest = object : JsonObjectRequest(
                    Method.POST, url, jsonObject,
                    { response ->
                        val nuevoMonedero = usuario.monedero - etCantidad.text.toString().toFloat()
                        usuario.monedero = nuevoMonedero

                        showAlertSucces("Apuesta realizada correctamente")

                    },
                    { error ->
                        Log.i("----->", error.toString())
                    }) {

                }
                if (usuario.monedero < etCantidad.text.toString().toFloat()) {
                    showAlertError("No hay dinero suficiente en tu cuenta")
                } else {
                    requestQueue.add(jsonObjectRequest)
                }

            }
        } else if (es_local == false) {
            tvCuota.setText(partido.cuotaVisitante.toString())
            Picasso.get().load(partido.visitante.logo).into(imagenEscudo)
            tvNombre.setText(getString(R.string.bottom_gana) + " " + partido.visitante.nombre)

            buttonApuesta.setOnClickListener {

                if (etCantidad.text.isEmpty() || etCantidad.text.toString()
                        .toInt() < 0 || etCantidad.text.toString().toInt() > 100000
                ) {
                    showAlertError(getString(R.string.error_cantidad))
                } else if (validacionLetras.matches(etCantidad.text.toString())) {
                    showAlertError("Solo puede contener números")
                } else {
                    buttonApuesta.isEnabled = false

                    val handler = Handler()
                    handler.postDelayed({
                        buttonApuesta.isEnabled = true
                    }, 5000)

                    var url = "http://10.59.135.44:3000/api/v1/futbol/guardarApuesta"

                    val jsonObject = JSONObject()

                    val requestQueue = Volley.newRequestQueue(context)

                    jsonObject.put("deporte", "Futbol")
                    jsonObject.put("tipo_apuesta", "Simple")
                    jsonObject.put("cuota", partido.cuotaLocal)
                    jsonObject.put("condicion", "visitante_gana")
                    jsonObject.put("usuarioId", usuario.id)
                    jsonObject.put("cantidad", etCantidad.text.toString().toInt())
                    jsonObject.put("partido_id", partido.id.toString())

                    val jsonObjectRequest = object : JsonObjectRequest(
                        Method.POST, url, jsonObject,
                        { response ->
                            val nuevoMonendero =
                                usuario.monedero - etCantidad.text.toString().toInt()
                            usuario.monedero = nuevoMonendero

                            showAlertSucces("Apuesta realizada correctamente")
                        },
                        { error ->
                            Log.i("----->", error.toString())
                        }) {

                    }
                    if (usuario.monedero < etCantidad.text.toString().toInt()) {
                        showAlertError("No hay dinero suficiente en tu cuenta")
                    } else {
                        requestQueue.add(jsonObjectRequest)
                    }
                }
            }
        }
    }

    private fun showAlertError(message: String) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialog)
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertSucces(message: String) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialog)
        builder.setTitle("Succesfull")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}