package com.example.royalbrave

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.royalbrave.modelos.Config
import com.example.royalbrave.modelos.Usuario
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import com.paypal.android.sdk.payments.PaymentConfirmation
import org.json.JSONException
import org.json.JSONObject
import java.math.BigDecimal
class MonederoFragment : Fragment() {
    private lateinit var cantidad: EditText
    private lateinit var bttnPayPal: Button

    var PAYPAL_REQUEST_CODE: Int = 7171
    var configuration: PayPalConfiguration = PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
        .clientId(Config.PAYPAL_CLIENT_ID)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_monedero, container, false)
    }

    override fun onDestroy() {
        requireContext().stopService(Intent(requireContext(), PayPalService::class.java))
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = arguments?.getSerializable("usuario") as? Usuario

        val dineroActual = view.findViewById<TextView>(R.id.textViewMonederoDinero)

        if (user != null){
            dineroActual.setText("$user!!.monedero€")
        }

        var intent = Intent(requireContext(), PayPalService::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration)
        requireContext().startService(intent)

        bttnPayPal = view.findViewById(R.id.buttonPayPal)
        cantidad = view.findViewById(R.id.editTextTextDineroPayPal)

        bttnPayPal.setOnClickListener {
            procesarPago()
        }
    }
    private fun procesarPago() {
        val cantidades = cantidad.text.toString()
        val payment = PayPalPayment(
            BigDecimal(cantidades),
            "EUR",
            "Pagado por X",
            PayPalPayment.PAYMENT_INTENT_SALE
        )

        val intent = Intent(requireContext(), PaymentActivity::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration)
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)

        startActivityForResult(intent, PAYPAL_REQUEST_CODE, null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK){
                var confirmation: PaymentConfirmation? = data!!.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
                if (confirmation != null){
                    try {
                        var paymentDetails: String = confirmation.toJSONObject().toString(4)
                        startActivity(Intent(requireContext(), MainActivity::class.java).putExtra("Payment Details", paymentDetails))
                    }catch (e: JSONException){
                        e.printStackTrace()
                    }
                }else if (resultCode == Activity.RESULT_CANCELED){
                    Toast.makeText(requireContext(), "Operación Cancelada", Toast.LENGTH_SHORT).show()
                }else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
                    Toast.makeText(requireContext(), "Operación Invalida", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}