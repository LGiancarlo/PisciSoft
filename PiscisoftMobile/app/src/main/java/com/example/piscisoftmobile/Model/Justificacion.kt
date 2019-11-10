package com.example.piscisoftmobile.Model

import java.io.Serializable


data class Justificacion(
    var codJustificacion:String? = "",
    var codReserva:String? = null,
    var fechaEnvio:String? = null,
    var motivo:String? = null,
    var fotoDocumento: String? = null,
    var validada: String? = null,
    var observaciones: String? = null
): Serializable