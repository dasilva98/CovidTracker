package pt.ulusofona.deisi.a2020.cm.g15.data.remote.requests


import com.google.gson.annotations.SerializedName

data class County(
    @SerializedName("data")
    val `data`: String,
    @SerializedName("concelho")
    val concelho: String,
    @SerializedName("distrito")
    val distrito: String,
    @SerializedName("incidencia")
    val incidencia: Int,
    @SerializedName("incidencia_risco")
    val incidenciaRisco: String,
    @SerializedName("casos_14dias")
    val casos14dias: Int,
    @SerializedName("population")
    val population: Int
)