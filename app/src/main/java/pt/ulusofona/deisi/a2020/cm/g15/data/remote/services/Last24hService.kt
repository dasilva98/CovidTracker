package pt.ulusofona.deisi.a2020.cm.g15.data.remote.services

import pt.ulusofona.deisi.a2020.cm.g15.data.remote.requests.County
import pt.ulusofona.deisi.a2020.cm.g15.data.remote.requests.LastUpdate
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Last24hService {

    @GET("get_last_update")
    fun getLastUpdate() : Call<LastUpdate>

    @GET("get_entry/{date}")
    fun getEntryDate(
        @Path("date")
        date : String
    ) : Call<LastUpdate>

    @GET("get_last_update_counties")
    fun getLastUpdateCounties() : Call<ArrayList<County>>
}