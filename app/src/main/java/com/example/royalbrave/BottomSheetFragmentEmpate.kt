package com.example.royalbrave

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.royalbrave.modelos.Partido
import com.example.royalbrave.modelos.Usuario
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import org.json.JSONObject

class BottomSheetFragmentEmpate(private val partido: Partido, private val es_local: Boolean) :
    BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_empate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var tvEquiposEmpate = view.findViewById<TextView>(R.id.textViewSheetEquiposEmpate)
        var tvCuotaEmpate = view.findViewById<TextView>(R.id.textViewSheetCuotaEmpate)

        var imagenLocal = view.findViewById<ImageView>(R.id.imageViewSheetLocal)
        var imagenVisitante = view.findViewById<ImageView>(R.id.imageViewSheetVisitante)

        var etCantidad = view.findViewById<EditText>(R.id.editTextSheetEmpateCantidad)
        var buttonApuesta = view.findViewById<Button>(R.id.buttonSheetEmpate)

        val validacionLetras = Regex("[a-zA-Z]+")

        val usuario = arguments?.getSerializable("usuario") as Usuario

        tvEquiposEmpate.setText(partido.local.nombre + " vs " + partido.visitante.nombre)
        tvCuotaEmpate.setText(partido.cuotaEmpate.toString())

        Picasso.get().load(partido.local.logo).into(imagenLocal)
        Picasso.get().load(partido.visitante.logo).into(imagenVisitante)

        buttonApuesta.setOnClickListener {
            if (etCantidad.text.isEmpty() || etCantidad.text.toString()
                    .toInt() < 0 || etCantidad.text.toString().toInt() > 100000
            ) {
                showAlertError(getString(R.string.error_cantidad))
            } else if (validacionLetras.matches(etCantidad.text.toString())) {
                showAlertError("Solo puede contener números")
            } else {
                var url = "http://royalbrave.es/api/v1/futbol/guardarApuesta"

                val jsonObject = JSONObject()

                val requestQueue = Volley.newRequestQueue(context)

                jsonObject.put("deporte", "Futbol")
                jsonObject.put("tipo_apuesta", "Simple")
                jsonObject.put("cuota", partido.cuotaEmpate)
                jsonObject.put("condicion", "empate")
                jsonObject.put("usuarioId", usuario.id)
                jsonObject.put("cantidad", etCantidad.text.toString().toInt())
                jsonObject.put("partido_id", partido.id.toString())

                val jsonObjectRequest = object : JsonObjectRequest(
                    Method.POST, url, jsonObject,
                    { response ->
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