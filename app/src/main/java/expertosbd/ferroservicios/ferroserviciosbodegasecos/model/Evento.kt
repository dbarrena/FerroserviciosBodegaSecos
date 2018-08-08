package expertosbd.ferroservicios.ferroserviciosbodegasecos.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Evento (
        @SerializedName("id_evento_bodega")
        val eventoBodegaID: Int,
        @SerializedName("fecha_evento")
        val fechaEvento: String,
        @SerializedName("nombre_cliente")
        val nombreCliente: String,
        val producto: String,
        val placas: String
): Serializable