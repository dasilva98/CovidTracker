package pt.ulusofona.deisi.a2020.cm.g15.domain

import android.util.Log
import kotlinx.coroutines.*
import pt.ulusofona.deisi.a2020.cm.g15.data.remote.requests.County
import pt.ulusofona.deisi.a2020.cm.g15.data.remote.services.Last24hService
import pt.ulusofona.deisi.a2020.cm.g15.data.sensors.location.LocationService
import pt.ulusofona.deisi.a2020.cm.g15.data.sensors.location.OnLocationChangedListener
import pt.ulusofona.deisi.a2020.cm.g15.ui.listeners.FetchDistrictObserver
import retrofit2.Retrofit
import retrofit2.awaitResponse
import java.io.IOException

private val TAG: String = InDangerLogic::class.java.simpleName

class InDangerLogic(private val retrofit: Retrofit){

    private var countyTemp = County("1","2","3",4,"5",6,7)
    private var counties = MutableList(1){countyTemp} as ArrayList<County>
    private var state = ""

    fun getInDangerLevel(callback: FetchDistrictObserver,internet: Boolean){
        if (internet){
            extractCountyAPI()
        }
        callback.onDistrictFetched(
            dangerLevelCalc(internet)
        )
    }

    fun setDistrict(callback: FetchDistrictObserver, district: String,internet: Boolean){
        state = district
        if (internet){
            extractCountyAPI()
        }
        callback.onDistrictFetched(
            dangerLevelCalc(internet)
        )
    }

    fun extractCountyAPI(){
        val service = retrofit.create(Last24hService::class.java)
        CoroutineScope(Dispatchers.IO).async {
            //Log.i(TAG,"Waiting Response")
            val request = service.getLastUpdateCounties().awaitResponse()
            //Log.i(TAG,"Received")

            if (request.isSuccessful){
                //Log.i(TAG,"Successful")
                counties = request.body()!!
            } else{
                Log.i(TAG,"UnSuccessful")
            }
        }
    }

    private fun dangerLevelCalc(internet: Boolean): String {
        Thread.sleep(100)
        return if (internet) {
            if (state == "" || counties.size == 1){
                "Online"
            }else{
                //Log.i(TAG,counties.single{it.concelho == state.toUpperCase()}.toString())
                try {
                    val county = counties.single{it.concelho == state.toUpperCase()}
                    county.incidenciaRisco
                }catch (e : NoSuchElementException){
                    "Fora de Portugal"
                }
            }
        } else {
            "Offline"
        }
    }
}