package com.example.royalbrave

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.royalbrave.modelos.Equipo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONArray

private var clasificacion: ArrayList<Equipo> = ArrayList()

class ClasificacionPremierFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clasificacion_premier, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val requestQueue = Volley.newRequestQueue(context)
        super.onViewCreated(view, savedInstanceState)

        val tvMaximosGoleadores = view.findViewById<TextView>(R.id.textViewMaximosGoleadoresPremier)

        GlobalScope.launch {
            val url = "http://royalbrave.es/api/v1/futbol/getClasificacionesHoy"

            val jsonObjectRequest = JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    val jsonArray = JSONArray(response.toString())
                    if (jsonArray.length() != 0) {
                        val jsonObj = jsonArray.getJSONObject(0)

                        val responseObj = jsonObj.getJSONObject("clasificacion_premier")
                            .getJSONArray("response").getJSONObject(0).getJSONObject("league")
                        val arrayEquipos = responseObj.getJSONArray("standings").getString(0)

                        val jsonArrayEquipos = JSONArray(arrayEquipos)
                        Log.i("---JSON----", jsonArrayEquipos.getJSONObject(0).toString())

                        clasificacion.clear()

                        for (i in 0 until jsonArrayEquipos.length()) {
                            val jsonObjectEquipo = jsonArrayEquipos.getJSONObject(i)
                            val rank = jsonObjectEquipo.getInt("rank")
                            val points = jsonObjectEquipo.getInt("points")
                            val equipoJSON = jsonObjectEquipo.getJSONObject("team")
                            val id = equipoJSON.getInt("id")
                            val name = equipoJSON.getString("name")
                            val logo = equipoJSON.getString("logo")

                            val equipo = Equipo(id, name, logo, rank, points, false)
                            clasificacion.add(equipo)
                        }
                    }else{
                        showAlert("No se ha cargado la clasificación de la Premier League")
                    }
                    MainScope().launch {
                        tvMaximosGoleadores.setOnClickListener {
                            findNavController().navigate(R.id.action_clasificacionPremierFragment_to_goleadoresPremierFragment)
                        }
                        val rvList =
                            view.findViewById<RecyclerView>(R.id.recyclerViewClasificacionPremier)
                        rvList.adapter = ListAdapterClasificacion(clasificacion, requireContext())
                        rvList.layoutManager = LinearLayoutManager(requireContext())
                        rvList.adapter?.notifyDataSetChanged()
                    }
                },
                { error ->
                    Log.i("---ERROOOR---->", error.toString())
                })
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