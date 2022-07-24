package pt.ulusofona.deisi.a2020.cm.g15.data.remote.services

import pt.ulusofona.deisi.a2020.cm.g15.data.remote.requests.Vaccine
import retrofit2.http.GET
import retrofit2.Call

interface VaccinesService {

    @GET("vaccines")
    fun getVaccines() : Call<ArrayList<Vaccine>>
}