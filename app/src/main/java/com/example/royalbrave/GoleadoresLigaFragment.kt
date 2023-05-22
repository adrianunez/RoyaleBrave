package com.example.royalbrave

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.royalbrave.modelos.Jugador
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONArray

class GoleadoresLigaFragment : Fragment() {
    private var jugadores: ArrayList<Jugador> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goleadores_liga, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch {
            val queue = Volley.newRequestQueue(context)
            val url = "http://royalbrave.es/api/v1/futbol/getTopScoresLiga"
            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET, url, null,
                { response ->
                    val jsonArray = JSONArray(response.toString())
                    if (jsonArray.length() != 0) {
                        val arrayJugadores = response.getJSONObject(0)
                            .getJSONObject("top_scores_laliga")
                            .getJSONArray("response")
                        jugadores.clear()
                        for (i in 0 until arrayJugadores.length()) {
                            val player = arrayJugadores.getJSONObject(i)
                                .getJSONObject("player")
                            val stats = arrayJugadores.getJSONObject(i)
                                .getJSONArray("statistics")
                                .getJSONObject(0)

                            val nombre = player.getString("name")
                            val foto = player.getString("photo")
                            var fotoEquipo = stats.getJSONObject("team")
                                .getString("logo")
                            val goles = stats.getJSONObject("goals")
                                .getInt("total")

                            var jugador = Jugador(i + 1, nombre, foto, fotoEquipo, goles)
                            jugadores.add(jugador)
                        }

                    } else {
                        showAlert("No se han podido cargar las clasificaciones de LaLiga")
                    }
                    MainScope().launch {
                        val rvList =
                            view.findViewById<RecyclerView>(R.id.recyclerViewGoleadoresLiga)
                        rvList.adapter = ListAdapterGoleadores(jugadores, requireContext())
                        rvList.layoutManager = LinearLayoutManager(requireContext())
                        rvList.adapter?.notifyDataSetChanged()
                    }
                },
                { error ->
                    Log.i("ERROR--->", error.toString())
                }
            )
            queue.add(jsonArrayRequest)
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