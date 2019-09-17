package com.example.piscisoftmobile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import android.widget.Toast

class ReservarFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_reservar, container, false)
        val calendarView : CalendarView = root.findViewById(R.id.calendario)

        //val btn_modificar: Button = root.findViewById(R.id.btn_modificar)


        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            val msg = "HOLA Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year
            Toast.makeText( context, msg, Toast.LENGTH_SHORT).show()
        }


        return root
    }
}