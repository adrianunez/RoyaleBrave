package com.example.royalbrave

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.royalbrave.modelos.Jugador
import com.example.royalbrave.modelos.Transacción
import com.example.royalbrave.modelos.Usuario
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class EstadisticasFragment : Fragment() {

    private var transacciones: ArrayList<Transacción> = ArrayList()
    private var totalAmount: Float = 0.0f


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_estadisticas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = arguments?.getSerializable("usuario") as? Usuario

        GlobalScope.launch {
            val requestQueue = Volley.newRequestQueue(context)
            val url = "http://royalbrave.es/api/v1/user/mostrarApuestas/" + user!!.id

            val jsonObjectRequest = JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    transacciones.clear()

                    var arrayUno = response.getJSONArray(0)
                    var arrayDos = response.getJSONArray(1)

                    if (arrayUno.length() != 0) {
                        for (i in 0 until arrayUno.length()) {
                            val transactionObject = arrayUno.getJSONObject(i)

                            val cantidadDinero = transactionObject.getDouble("cantidad").toFloat()
                            totalAmount += cantidadDinero
                        }
                        for (i in 0 until 5) {
                            val transactionObject = arrayUno.getJSONObject(i)

                            val cantidad = transactionObject.getDouble("cantidad").toFloat()

                            val esPagado = transactionObject.getBoolean("es_pagado")

                            val fechaRealizada = transactionObject.getString("fehca_realizada")
                            val formatoOriginal = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                            val fecha = formatoOriginal.parse(fechaRealizada)
                            val formatoNuevo = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
                            val fechaFormateada = formatoNuevo.format(fecha)

                            val idApuestaDeportes = transactionObject.getInt("id_apuesta_deportes")

                            val deporte = arrayDos.getJSONArray(i).getJSONObject(0).getString("deporte")
                            val status = arrayDos.getJSONArray(i).getJSONObject(0).getString("status")

                            val transaction = Transacción(
                                fechaFormateada,
                                cantidad,
                                status,
                                deporte,
                                esPagado,
                                idApuestaDeportes
                            )
                            transacciones.add(transaction)
                        }
                        val sortedList = transacciones.sortedByDescending { it.fecha_realizada }
                        val transaccionesOrdenadas = ArrayList(sortedList)

                        val totalGastadoDecimales = String.format("%.2f", totalAmount)
                        val tvTotalGastado = view.findViewById<TextView>(R.id.textViewTotalGastos)
                        tvTotalGastado.setText("$totalGastadoDecimales€")


                        MainScope().launch {

                            val rvList =
                                view.findViewById<RecyclerView>(R.id.recyclerViewUltimasApuestas)
                            rvList.adapter = ListAdapterEstadisticasApuestas(
                                transaccionesOrdenadas,
                                findNavController(),
                                requireContext()
                            )
                            rvList.layoutManager = LinearLayoutManager(requireContext())
                            rvList.adapter?.notifyDataSetChanged()
                        }
                    } else {
                        showAlert("No se han cargado transacciones")
                    }
                },
                { error ->
                    showAlert("No se han cargado transacciones")
                    Log.i("---ERROOOR---->", error.toString())
                }).apply {
                retryPolicy = DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            }
            requestQueue.add(jsonObjectRequest)
        }
    }

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialog)
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}