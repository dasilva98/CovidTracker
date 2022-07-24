package pt.ulusofona.deisi.a2020.cm.g15.data.repositories

import android.util.Log
import kotlinx.coroutines.*
import pt.ulusofona.deisi.a2020.cm.g15.data.remote.requests.LastUpdate
import pt.ulusofona.deisi.a2020.cm.g15.data.remote.services.Last24hService
import pt.ulusofona.deisi.a2020.cm.g15.data.room.dao.Last24hDao
import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.LastUpdateEnt
import retrofit2.Retrofit
import retrofit2.awaitResponse
import kotlin.collections.HashMap

private val TAG: String = Last24hRepository::class.java.simpleName

class Last24hRepository(private val retrofit: Retrofit, private val last24hDao: Last24hDao) {
    private val hashMap:HashMap<String,Int> = hashMapOf("0" to 0)
    private val hashMapStr:HashMap<String,String> = hashMapOf("0" to "0")

    private var todayApiNumber = "0"
    private var yesterdayApiNumber = "0"

    private var todayUpdate = LastUpdate(hashMap,hashMapStr,hashMap,hashMap,hashMap,hashMap)
    private var yesterdayUpdate = LastUpdate(hashMap,hashMapStr,hashMap,hashMap,hashMap,hashMap)

    private var todaylastUpdateEnt = LastUpdateEnt(0,"0",0,0,0,0,0)
    private var yesterdaylastUpdateEnt = LastUpdateEnt(1,"0",0,0,0,0,0)

    fun getLast24hOperations(internet:Boolean,today: String,yesterday: String):Pair<LastUpdateEnt,LastUpdateEnt>{
        if (internet) {
            updateLast24hApi(today,yesterday)
            return getCachedLast24h()
        }else{
            return getCachedLast24h()
        }
    }

    private fun updateLast24hApi(today:String, yesterday:String){

        val service = retrofit.create(Last24hService::class.java)

        CoroutineScope(Dispatchers.IO).async {
            //Log.i(TAG,"Waiting Response")
            val requestToday = service.getEntryDate(today).awaitResponse()
            val requestYesterday = service.getEntryDate(yesterday).awaitResponse()
            //Log.i(TAG,"Received")
            if (requestToday.isSuccessful && requestYesterday.isSuccessful){
                //Log.i(TAG,"Successful")
                todayUpdate = requestToday.body()!!
                yesterdayUpdate = requestYesterday.body()!!

                for((key, _) in todayUpdate.confirmadosNovos){
                    todayApiNumber = key
                    break
                }
                yesterdayApiNumber = (todayApiNumber.toInt() - 1).toString()

                val todayLastUpdate = LastUpdateEnt(
                    0,
                    todayUpdate.data[todayApiNumber].toString(),
                    todayUpdate.confirmadosNovos[todayApiNumber]!!,
                    todayUpdate.recuperados[todayApiNumber]!!,
                    todayUpdate.obitos[todayApiNumber]!!,
                    todayUpdate.internados[todayApiNumber]!!,
                    todayUpdate.internadosUci[todayApiNumber]!!
                    )
                last24hDao.insert(todayLastUpdate)
                val yesterdayLastUpdate = LastUpdateEnt(
                    1,
                    yesterdayUpdate.data[yesterdayApiNumber]!!,
                    yesterdayUpdate.confirmadosNovos[yesterdayApiNumber]!!,
                    yesterdayUpdate.recuperados[yesterdayApiNumber]!!,
                    yesterdayUpdate.obitos[yesterdayApiNumber]!!,
                    yesterdayUpdate.internados[yesterdayApiNumber]!!,
                    yesterdayUpdate.internadosUci[yesterdayApiNumber]!!
                )
                last24hDao.insert(yesterdayLastUpdate)
            } else {
                Log.i(TAG,"UnSuccessful")

            }
        }
    }

    private fun getCachedLast24h():Pair<LastUpdateEnt,LastUpdateEnt>{
        GlobalScope.launch(Dispatchers.IO) {
            val last24hList = last24hDao.getAll()
            if (last24hList.isEmpty()){
                todaylastUpdateEnt = LastUpdateEnt(0,"0",0,0,0,0,0)
                yesterdaylastUpdateEnt = LastUpdateEnt(1,"0",0,0,0,0,0)
            }else{
                todaylastUpdateEnt = last24hDao.getById(0)
                yesterdaylastUpdateEnt = last24hDao.getById(1)
            }

        }
        Thread.sleep(50)
        return Pair(todaylastUpdateEnt,yesterdaylastUpdateEnt)
    }

}