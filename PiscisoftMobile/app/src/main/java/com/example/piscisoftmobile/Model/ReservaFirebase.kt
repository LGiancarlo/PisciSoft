package com.example.piscisoftmobile.Model

import android.util.Log
import android.widget.Toast
import com.example.piscisoftmobile.HistorialFragment
import com.example.piscisoftmobile.OnDataFinishedListener
import com.google.firebase.firestore.FirebaseFirestore

class ReservaFirebase {

    val db = FirebaseFirestore.getInstance()
    val ref = db.collection("reserva")

    fun existeReservaEsteDia(listener:OnDataFinishedListener, codUsuario: String, codTurno: String){ //Verificar si existe reserva este día
        var fechaANoRepetir = codTurno.substring(0, 10)
        val query = ref.whereEqualTo("codUsuario",codUsuario)
        query.get()
            .addOnSuccessListener { documents ->
                var existe = false
                for (document in documents) {
                    val reserva = document.toObject(Reserva::class.java)
                    if (reserva.codTurno!!.contains(fechaANoRepetir,false)){
                        existe = true
                        break
                    }
                }
                listener.OnVerificacionFinished(existe)
            }
    }

    fun registrarReserva(listener: OnDataFinishedListener, reserva: Reserva){
        val db = FirebaseFirestore.getInstance()
        db.collection("reserva").add(reserva)
        actualizarCapacidad(reserva.codTurno!!, "Disminuir")
        listener.OnRegistroReservaFinished()
    }

    fun actualizarCapacidad(codTurno:String, modo:String){
        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("turno").document(codTurno)

        ref.get()
            .addOnSuccessListener { document ->
                val turno = document.toObject(Turno::class.java)
                if (modo == "Disminuir"){
                    ref.update("capacidadCubierta", turno!!.capacidadCubierta!!+1)
                } else {
                    ref.update("capacidadCubierta", turno!!.capacidadCubierta!!-1)
                    if (turno.estado == "Cerrado" && turno.observaciones == "Turno lleno") {
                        ref.update("estado","Abierto")
                        ref.update("observaciones","")
                    }
                }
            }
    }

    fun existenReservas(listener: OnDataFinishedListener, codigo:String){
        val query = ref.whereEqualTo("codUsuario",codigo)
        query.get()
            .addOnSuccessListener { documents ->
                if ( ! documents.isEmpty ) {
                    listener.OnVerificacionFinished(true)
                } else {
                    listener.OnVerificacionFinished(false)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("ERROR FIREBASE", "Error getting documents: ", exception)
            }
    }

    fun obtenerReservasByUsuario(listener: OnDataFinishedListener, codigo:String){
        val query = ref.whereEqualTo("codUsuario",codigo)
        query.get()
            .addOnSuccessListener { documents ->
                var reservas = mutableListOf<Reserva>()
                for (document in documents) {
                    val reserva = document.toObject(Reserva::class.java)
                    reserva.codReserva = document.id
                    reservas.add(reserva)
                }
                //reservas.sortBy { reserva -> reserva.fecha?.substring(reserva.fecha?.indexOf("-")!! + 1, reserva.fecha?.indexOf("-")!!)!!;}
                listener.OnListaReservasDataFinished(reservas)
            }
            .addOnFailureListener { exception ->
                Log.w("ERROR FIREBASE", "Error getting documents: ", exception)
            }
    }

    fun obtenerReservasEnTurno(listener:OnDataFinishedListener, codTurno:String){
        val db = FirebaseFirestore.getInstance()
        db.collection("reserva").whereEqualTo("codTurno",codTurno)
            .get()
            .addOnSuccessListener { documents ->
                var reservas = mutableListOf<Reserva>()
                for (document in documents){
                    val reserva = document.toObject(Reserva::class.java)
                    reservas.add(reserva)
                }
                listener.OnListaReservasDataFinished(reservas)
            }
            .addOnFailureListener{ exception ->
                Log.d("ERROR EN FIREBASE", "get failed with ", exception)
            }
    }
    fun eliminarReserva(reserva:Reserva){
        actualizarCapacidad(reserva.codTurno!!, "Aumentar")
        ref.document(reserva.codReserva!!).delete()
    }


}
