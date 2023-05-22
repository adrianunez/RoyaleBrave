package com.example.royalbrave

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment(val listener:(año:Int, mes:Int, dia:Int) -> Unit): DialogFragment(), DatePickerDialog.OnDateSetListener
{
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val c = Calendar.getInstance()
            var año = c.get(Calendar.YEAR)
            var mes = c.get(Calendar.MONTH)
            var dia = c.get(Calendar.DAY_OF_MONTH)

            return DatePickerDialog(requireActivity(),R.style.datePickerStyle, this, año, mes , dia)
        }
        override fun onDateSet(p0: DatePicker?, año: Int, mes: Int, dia: Int) {
            listener(año, mes , dia)
        }
}