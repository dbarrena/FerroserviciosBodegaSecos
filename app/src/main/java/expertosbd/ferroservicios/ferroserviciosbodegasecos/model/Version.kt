package expertosbd.ferroservicios.ferroserviciosbodegasecos.model

import com.google.gson.annotations.SerializedName

data class Version (
        @SerializedName("app_version")
        val appVersion: String
)