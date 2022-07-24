package pt.ulusofona.deisi.a2020.cm.g15.data.remote.requests


import com.google.gson.annotations.SerializedName

data class Vaccine(
    @SerializedName("Data")
    val `data`: Long,
    @SerializedName("Inoculacao1_Ac")
    val inoculacao1Ac: Int,
    @SerializedName("Inoculacao2_Ac")
    val inoculacao2Ac: Int,
    @SerializedName("Vacinados_Ac")
    val vacinadosAc: Int
)