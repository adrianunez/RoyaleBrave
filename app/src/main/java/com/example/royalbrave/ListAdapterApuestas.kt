package com.example.royalbrave

import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.royalbrave.modelos.Apuesta
import com.example.royalbrave.modelos.Partido
import com.example.royalbrave.modelos.Usuario
import org.json.JSONObject

class ApuestaAdapter(private val apuestas: List<Apuesta>, val partido: Partido, val usuario: Usuario, val context: Context) :
    RecyclerView.Adapter<ApuestaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_apuestas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val apuesta = apuestas[position]
        holder.titulo.text = apuesta.titulo

        holder.titulo.setOnClickListener {
            apuesta.visible = !apuesta.visible
            holder.botones.visibility = if (apuesta.visible) View.VISIBLE else View.GONE

            if (holder.expandido){
                holder.imagen.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                holder.expandido = false
            }else{
                holder.imagen.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                holder.expandido = true
            }
        }
        holder.imagen.setOnClickListener {
            apuesta.visible = !apuesta.visible
            holder.botones.visibility = if (apuesta.visible) View.VISIBLE else View.GONE

            if (holder.expandido){
                holder.imagen.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                holder.expandido = false
            }else{
                holder.imagen.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                holder.expandido = true
            }
        }
        holder.constraintApuesta.setOnClickListener {
            apuesta.visible = !apuesta.visible
            holder.botones.visibility = if (apuesta.visible) View.VISIBLE else View.GONE

            if (holder.expandido){
                holder.imagen.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                holder.expandido = false
            }else{
                holder.imagen.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                holder.expandido = true
            }
        }


        Log.i("APUESTA", apuesta.titulo.toString() + apuesta.opciones.size.toString())

        if (apuesta.opciones.size == 3){
            holder.opcion1.text = apuesta.opciones.get(0)
            holder.opcion2.text = apuesta.opciones.get(1)
            holder.opcion3.text = apuesta.opciones.get(2)
            holder.opcion2.visibility = View.VISIBLE

        }else if (apuesta.opciones.size == 2){
            holder.opcion1.text = apuesta.opciones.get(0)
            holder.opcion3.text = apuesta.opciones.get(1)
            holder.opcion2.visibility = View.GONE
        }

        if (apuesta.titulo == "Resultado Exacto" || apuesta.titulo == "Resultat Exacte" || apuesta.titulo == "Exact Result"){
            //Gana Local
            holder.opcion1.setOnClickListener {
                holder.opcion1.isEnabled = false
                holder.opcion2.isEnabled = false
                holder.opcion3.isEnabled = false

                val handler = Handler()
                handler.postDelayed({
                    holder.opcion1.isEnabled = true
                    holder.opcion2.isEnabled = true
                    holder.opcion3.isEnabled = true
                }, 5000)
                apostarSimple(holder, "local_gana", partido.cuotaLocal)
            }
            //Empatan
            holder.opcion2.setOnClickListener {
                holder.opcion1.isEnabled = false
                holder.opcion2.isEnabled = false
                holder.opcion3.isEnabled = false

                val handler = Handler()
                handler.postDelayed({
                    holder.opcion1.isEnabled = true
                    holder.opcion2.isEnabled = true
                    holder.opcion3.isEnabled = true
                }, 5000)
                apostarSimple(holder, "empate", partido.cuotaEmpate)
            }
            //Gana Visitante
            holder.opcion3.setOnClickListener {
                holder.opcion1.isEnabled = false
                holder.opcion2.isEnabled = false
                holder.opcion3.isEnabled = false

                val handler = Handler()
                handler.postDelayed({
                    holder.opcion1.isEnabled = true
                    holder.opcion2.isEnabled = true
                    holder.opcion3.isEnabled = true
                }, 5000)
                apostarSimple(holder, "visitante_gana", partido.cuotaVisitante)
            }
        }
        if (apuesta.titulo == "Goles +-2.5" || apuesta.titulo == "Gols +-2.5" || apuesta.titulo == "Goals +-2.5"){
            //Mas 2.5 Goles
            holder.opcion1.setOnClickListener {
                holder.opcion1.isEnabled = false
                holder.opcion3.isEnabled = false

                val handler = Handler()
                handler.postDelayed({
                    holder.opcion1.isEnabled = true
                    holder.opcion3.isEnabled = true
                }, 5000)
                apostarDetalle(holder, "mas_goles", partido.cuotaMasGoles)
            }
            //Menos 2.5 Goles
            holder.opcion3.setOnClickListener {
                holder.opcion1.isEnabled = false
                holder.opcion3.isEnabled = false

                val handler = Handler()
                handler.postDelayed({
                    holder.opcion1.isEnabled = true
                    holder.opcion3.isEnabled = true
                }, 5000)
                apostarDetalle(holder, "menos_goles", partido.cuotaMenosGoles)
            }
        }
        if (apuesta.titulo == "Tarjetas Amarillas +-3.5" || apuesta.titulo == "Targetes Grogues +-3.5" ||apuesta.titulo == "Yellow Cards +-3.5"){
            //Mas de 3.5 amarillas
            holder.opcion1.setOnClickListener {
                holder.opcion1.isEnabled = false
                holder.opcion3.isEnabled = false

                val handler = Handler()
                handler.postDelayed({
                    holder.opcion1.isEnabled = true
                    holder.opcion3.isEnabled = true
                }, 5000)
                apostarDetalle(holder, "mas_amarillas", partido.cuotaMasAmarillas)
            }
            //Menos de 3.amarillas
            holder.opcion3.setOnClickListener {
                holder.opcion1.isEnabled = false
                holder.opcion3.isEnabled = false

                val handler = Handler()
                handler.postDelayed({
                    holder.opcion1.isEnabled = true
                    holder.opcion3.isEnabled = true
                }, 5000)
                apostarDetalle(holder, "menos_amarillas", partido.cuotaMenosAmarillas)
            }
        }
        if (apuesta.titulo == "Tarjetas Rojas +-1.5" || apuesta.titulo == "Targetes vermelles +-1.5" || apuesta.titulo == "Red Cards +-1.5"){
            //Mas de 1.5 rojas
            holder.opcion1.setOnClickListener {
                holder.opcion1.isEnabled = false
                holder.opcion3.isEnabled = false

                val handler = Handler()
                handler.postDelayed({
                    holder.opcion1.isEnabled = true
                    holder.opcion3.isEnabled = true
                }, 5000)
                apostarDetalle(holder, "mas_rojas", partido.cuotaMasRojas)
            }
            //Menos de 1.5 rojas
            holder.opcion3.setOnClickListener {
                holder.opcion1.isEnabled = false
                holder.opcion3.isEnabled = false

                val handler = Handler()
                handler.postDelayed({
                    holder.opcion1.isEnabled = true
                    holder.opcion3.isEnabled = true
                }, 5000)
                apostarDetalle(holder, "menos_rojas", partido.cuotaMenosRojas)
            }
        }
        if (apuesta.titulo == "Corners +-8.5"){
            //Mas de 8.5 corners
            holder.opcion1.setOnClickListener {
                holder.opcion1.isEnabled = false
                holder.opcion3.isEnabled = false

                val handler = Handler()
                handler.postDelayed({
                    holder.opcion1.isEnabled = true
                    holder.opcion3.isEnabled = true
                }, 5000)
                apostarDetalle(holder, "mas_corners", partido.cuotaMasCorners)
            }
            //Menos de 1.5 rojas
            holder.opcion3.setOnClickListener {
                holder.opcion1.isEnabled = false
                holder.opcion3.isEnabled = false

                val handler = Handler()
                handler.postDelayed({
                    holder.opcion1.isEnabled = true
                    holder.opcion3.isEnabled = true
                }, 5000)
                apostarDetalle(holder, "menos_corners", partido.cuotaMenosCorners)
            }
        }
        if (apuesta.titulo == "Tiros a puerta +-6.5" || apuesta.titulo == "Tirs a porta +-6.5" || apuesta.titulo == "Shots on goal +-6.5"){
            //Mas de 6.5 tiros
            holder.opcion1.setOnClickListener {
                holder.opcion1.isEnabled = false
                holder.opcion3.isEnabled = false

                val handler = Handler()
                handler.postDelayed({
                    holder.opcion1.isEnabled = true
                    holder.opcion3.isEnabled = true
                }, 5000)
                apostarDetalle(holder, "mas_tiros", partido.cuotaMasChutes)
            }
            //Menos de 6.5 tiros
            holder.opcion3.setOnClickListener {
                holder.opcion1.isEnabled = false
                holder.opcion3.isEnabled = false

                val handler = Handler()
                handler.postDelayed({
                    holder.opcion1.isEnabled = true
                    holder.opcion3.isEnabled = true
                }, 5000)
                apostarDetalle(holder, "menos_tiros", partido.cuotaMenosChutes)
            }
        }
    }

    override fun getItemCount() = apuestas.size

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
    private fun apostarSimple(holder: ViewHolder, condicion: String, cuota: Float){
        val validacionLetras = Regex("[a-zA-Z]+")
        if (holder.etCantidad.text.isEmpty() || holder.etCantidad.text.toString().toInt() < 0 || holder.etCantidad.text.toString().toInt() > 100000) {
            showAlertError(context.getString(R.string.error_cantidad))
        }else if(validacionLetras.matches(holder.etCantidad.text.toString())){
            showAlertError("Solo puede contener números")
        } else{
            var url = "http://royalbrave.es/api/v1/futbol/guardarApuesta"

            val jsonObject = JSONObject()

            val requestQueue = Volley.newRequestQueue(context)

            jsonObject.put("deporte", "Futbol")
            jsonObject.put("tipo_apuesta", "Simple")
            jsonObject.put("cuota", cuota)
            jsonObject.put("condicion", condicion)
            jsonObject.put("usuarioId", usuario.id)
            jsonObject.put("cantidad", holder.etCantidad.text.toString().toInt())
            jsonObject.put("partido_id", partido.id.toString())

            val jsonObjectRequest = object : JsonObjectRequest(
                Method.POST, url, jsonObject,
                { response ->
                    val nuevoMonendero = usuario.monedero - holder.etCantidad.text.toString().toInt()
                    usuario.monedero = nuevoMonendero

                    showAlertSucces("Apuesta realizada correctamente")
                },
                { error ->
                    Log.i("----->", error.toString())
                }) {

            }

            if (usuario.monedero < holder.etCantidad.text.toString().toInt()){
                showAlertError("No hay dinero suficiente en tu cuenta")
            }else{
                requestQueue.add(jsonObjectRequest)
            }
        }
    }

    private fun apostarDetalle(holder: ViewHolder, condicion: String, cuota: Float){
        val validacionLetras = Regex("[a-zA-Z]+")
        if (holder.etCantidad.text.isEmpty() || holder.etCantidad.text.toString().toInt() < 0 || holder.etCantidad.text.toString().toInt() > 100000) {
            showAlertError(context.getString(R.string.error_cantidad))
        }else if(validacionLetras.matches(holder.etCantidad.text.toString())){
            showAlertError("Solo puede contener números")
        } else{
            var url = "http://royalbrave.es/api/v1/futbol/guardarApuesta"

            val jsonObject = JSONObject()

            val requestQueue = Volley.newRequestQueue(context)

            jsonObject.put("deporte", "Futbol")
            jsonObject.put("tipo_apuesta", "Detalle")
            jsonObject.put("cuota", cuota)
            jsonObject.put("condicion", condicion)
            jsonObject.put("usuarioId", usuario.id)
            jsonObject.put("cantidad", holder.etCantidad.text.toString().toInt())
            jsonObject.put("partido_id", partido.id.toString())

            val jsonObjectRequest = object : JsonObjectRequest(
                Method.POST, url, jsonObject,
                { response ->
                    val nuevoMonendero = usuario.monedero - holder.etCantidad.text.toString().toInt()
                    usuario.monedero = nuevoMonendero

                    showAlertSucces("Apuesta realizada correctamente")
                },
                { error ->
                    Log.i("----->", error.toString())
                }) {

            }
            if (usuario.monedero < holder.etCantidad.text.toString().toInt()){
                showAlertError("No hay dinero suficiente en tu cuenta")
            }else{
                requestQueue.add(jsonObjectRequest)
            }
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val titulo = view.findViewById<TextView>(R.id.textViewApuestaTitulo)
        val botones = view.findViewById<ConstraintLayout>(R.id.apuesta_botones)
        val opcion1 = view.findViewById<Button>(R.id.apuesta_opcion1)
        val opcion2 = view.findViewById<Button>(R.id.apuesta_opcion2)
        val opcion3 = view.findViewById<Button>(R.id.apuesta_opcion3)
        val etCantidad = view.findViewById<EditText>(R.id.editTextTextDetallesPartidoCantidad)
        val imagen = view.findViewById<ImageView>(R.id.imageViewFlecha)
        var expandido = false
        var constraintApuesta = view.findViewById<ConstraintLayout>(R.id.relativeLayout)
    }
}
