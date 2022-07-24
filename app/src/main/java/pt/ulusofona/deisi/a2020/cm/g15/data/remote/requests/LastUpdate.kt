package pt.ulusofona.deisi.a2020.cm.g15.data.remote.requests


import com.google.gson.annotations.SerializedName

data class LastUpdate(
    @SerializedName("confirmados_novos")
    val confirmadosNovos:HashMap<String,Int>,
    @SerializedName("data")
    val `data`: HashMap<String,String>,
    @SerializedName("internados")
    val internados: HashMap<String,Int>,
    @SerializedName("internados_uci")
    val internadosUci: HashMap<String,Int>,
    @SerializedName("obitos")
    val obitos: HashMap<String,Int>,
    @SerializedName("recuperados")
    val recuperados: HashMap<String,Int>
)