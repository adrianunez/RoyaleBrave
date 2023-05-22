package com.example.royalbrave

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.royalbrave.modelos.Equipo
import com.example.royalbrave.modelos.Partido
import com.example.royalbrave.modelos.Usuario
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import java.math.BigDecimal
import kotlin.random.Random

private const val ARG_PARAM1 = "param1"

class PPConLogFragment : Fragment() {
    private var param1: Usuario? = null
    private var partidos: ArrayList<Partido> = ArrayList()
    lateinit var tvMonedero: TextView

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
        return inflater.inflate(R.layout.fragment_p_p_con_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(requireActivity()) {
        }
        callback.isEnabled = false

        var imPerfil = view.findViewById<ImageView>(R.id.imageViewPPLogPerfil)
        tvMonedero = view.findViewById(R.id.textViewMonderoLog)
        var imagenLaLiga = view.findViewById<ImageView>(R.id.imageViewLaLiga)
        var imagenPremier = view.findViewById<ImageView>(R.id.imageViewPremierLeague)

        val user = arguments?.getSerializable("usuario") as? Usuario

        if (user?.fotoPerfil != null){
            imPerfil.setImageBitmap(byteArrayToBitmap(user!!.fotoPerfil!!))
        }


        imagenLaLiga.setOnClickListener {
            findNavController().navigate(R.id.action_PPConLogFragment_to_clasificacionLaLigaFragment)
        }
        imagenPremier.setOnClickListener {
            findNavController().navigate(R.id.action_PPConLogFragment_to_clasificacionPremierFragment)
        }

        if (user != null) {
            Log.i("--USER--", user.nombre)
            tvMonedero.setText(user!!.monedero.toString() + "€")
        } else {
            tvMonedero.setText("0€")
        }

        imPerfil.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("usuario", user)
            findNavController().navigate(R.id.action_PPConLogFragment_to_perfilFragment, bundle)
        }

        GlobalScope.launch {
            val requestQueue = Volley.newRequestQueue(context)
            val url = "http://royalbrave.es/api/v1/futbol/getPartidosHoy"

            val jsonObjectRequest = JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    partidos.clear()

                    val jsonArray = JSONArray(response.toString())

                    if (jsonArray.length() != 0) {

                        val jsonObj = jsonArray.getJSONObject(0).getJSONObject("partidos_hoy")

                        for (key in jsonObj.keys()) {
                            val obj = jsonObj.getJSONObject(key)

                            val objFixture = jsonObj.getJSONObject(key)
                                .getJSONObject("fixture")

                            val id = objFixture.getInt("id")

                            val date = objFixture.getString("date")

                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
                            val localDateTime = LocalDateTime.parse(date, formatter)

                            val fecha =
                                localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                            val hora = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))


                            val objLeague = jsonObj.getJSONObject(key)
                                .getJSONObject("league")
                            val logoLiga = objLeague.getString("logo")
                            val nombreLiga = objLeague.getString("name")

                            val campo = objFixture.getJSONObject("venue").getString("name")

                            val equipoLocalJSON = obj.getJSONObject("teams").getJSONObject("home")
                            val idEquipoLocal = equipoLocalJSON.getInt("id")
                            val nomEquipoLocal = equipoLocalJSON.getString("name")
                            val logoEquipoLocal = equipoLocalJSON.getString("logo")

                            val equipoVisitanteJSON =
                                obj.getJSONObject("teams").getJSONObject("away")
                            val idEquipoVisitante = equipoVisitanteJSON.getInt("id")
                            val nomEquipoVisitante = equipoVisitanteJSON.getString("name")
                            val logoEquipoVisitante = equipoVisitanteJSON.getString("logo")

                            val equipoLocal = Equipo(idEquipoLocal, nomEquipoLocal, logoEquipoLocal)
                            val equipoVisitante =
                                Equipo(idEquipoVisitante, nomEquipoVisitante, logoEquipoVisitante)

                            val cuotaLocal = BigDecimal(Random.nextDouble(1.01, 4.0)).setScale(
                                2,
                                BigDecimal.ROUND_HALF_UP
                            ).toFloat()
                            val cuotaVisitante = BigDecimal(Random.nextDouble(1.01, 4.0)).setScale(
                                2,
                                BigDecimal.ROUND_HALF_UP
                            ).toFloat()
                            val cuotaEmpate = BigDecimal(Random.nextDouble(1.01, 5.0)).setScale(
                                2,
                                BigDecimal.ROUND_HALF_UP
                            ).toFloat()

                            val cuotaMasGoles = BigDecimal(Random.nextDouble(1.01, 4.0)).setScale(
                                2,
                                BigDecimal.ROUND_HALF_UP
                            ).toFloat()
                            val cuotaMenosGoles = BigDecimal(Random.nextDouble(1.01, 4.0)).setScale(
                                2,
                                BigDecimal.ROUND_HALF_UP
                            ).toFloat()

                            val cuotaMasAmarillas =
                                BigDecimal(Random.nextDouble(1.01, 4.0)).setScale(
                                    2,
                                    BigDecimal.ROUND_HALF_UP
                                ).toFloat()
                            val cuotaMenosAmarillas =
                                BigDecimal(Random.nextDouble(1.01, 4.0)).setScale(
                                    2,
                                    BigDecimal.ROUND_HALF_UP
                                ).toFloat()

                            val cuotaMasRojas = BigDecimal(Random.nextDouble(3.01, 4.0)).setScale(
                                2,
                                BigDecimal.ROUND_HALF_UP
                            ).toFloat()
                            val cuotaMenosRojas =
                                BigDecimal(Random.nextDouble(1.01, 2.00)).setScale(
                                    2,
                                    BigDecimal.ROUND_HALF_UP
                                ).toFloat()

                            val cuotaMasCorners =
                                BigDecimal(Random.nextDouble(2.51, 4.50)).setScale(
                                    2,
                                    BigDecimal.ROUND_HALF_UP
                                ).toFloat()
                            val cuotaMenosCorners =
                                BigDecimal(Random.nextDouble(1.01, 2.50)).setScale(
                                    2,
                                    BigDecimal.ROUND_HALF_UP
                                ).toFloat()

                            val cuotaMasChutes = BigDecimal(Random.nextDouble(2.51, 4.0)).setScale(
                                2,
                                BigDecimal.ROUND_HALF_UP
                            ).toFloat()
                            val cuotaMenosChutes =
                                BigDecimal(Random.nextDouble(1.01, 2.50)).setScale(
                                    2,
                                    BigDecimal.ROUND_HALF_UP
                                ).toFloat()

                            val partido = Partido(
                                id,
                                fecha,
                                hora,
                                campo,
                                equipoLocal,
                                equipoVisitante,
                                logoLiga,
                                nombreLiga,
                                cuotaLocal,
                                cuotaVisitante,
                                cuotaEmpate,
                                cuotaMasGoles,
                                cuotaMenosGoles,
                                cuotaMasAmarillas,
                                cuotaMenosAmarillas,
                                cuotaMasRojas,
                                cuotaMenosRojas,
                                cuotaMasCorners,
                                cuotaMenosCorners,
                                cuotaMasChutes,
                                cuotaMenosChutes
                            )

                            partidos.add(partido)
                        }

                        MainScope().launch {
                            val rvList = view.findViewById<RecyclerView>(R.id.recyclerViewPartidos)
                            rvList.adapter = ListAdapterPartidos(
                                partidos,
                                user!!,
                                findNavController(),
                                requireContext()
                            )
                            rvList.layoutManager = LinearLayoutManager(requireContext())
                            rvList.adapter?.notifyDataSetChanged()
                        }
                    } else {
                        showAlert("No se han cargado partidos")
                    }
                },
                { error ->
                    showAlert("No se han cargado partidos")
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
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}