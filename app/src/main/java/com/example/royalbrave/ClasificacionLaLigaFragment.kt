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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ClasificacionLaLigaFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var clasificacion: ArrayList<Equipo> = ArrayList()

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
        return inflater.inflate(R.layout.fragment_clasificacion_la_liga, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val requestQueue = Volley.newRequestQueue(context)
        super.onViewCreated(view, savedInstanceState)

        val tvMaximosGoleadores = view.findViewById<TextView>(R.id.textViewMaximosGoleadoresLiga)

        GlobalScope.launch {
            val url = "http://royalbrave.es/api/v1/futbol/getClasificacionesHoy"

            val jsonObjectRequest = JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                { response ->

                    //Accedes al JSONArray que nos devuelve la API
                    val jsonArray = JSONArray(response.toString())
                    if (jsonArray.length() != 0) {
                        val jsonObj = jsonArray.getJSONObject(0)

                        //Leemos la variable 'response' del JSON del partido y cogemos la clasimicacion de LaLiga
                        val responseObj = jsonObj.getJSONObject("clasificacion_laliga")
                            .getJSONArray("response").getJSONObject(0).getJSONObject("league")
                        val arrayEquipos = responseObj.getJSONArray("standings").getString(0)

                        val jsonArrayEquipos = JSONArray(arrayEquipos)

                        clasificacion.clear()

                        //ForEach que permite leer todos los equipos del JSON
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
                    }else {
                        showAlert("No se ha cargado la clasificación de LaLiga")
                    }
                    MainScope().launch {

                        tvMaximosGoleadores.setOnClickListener {
                            findNavController().navigate(R.id.action_clasificacionLaLigaFragment_to_goleadoresLigaFragment)
                        }
                        val rvList = view.findViewById<RecyclerView>(R.id.recyclerViewLaLiga)
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