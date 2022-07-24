package pt.ulusofona.deisi.a2020.cm.g15.data.repositories

import android.util.Log
import kotlinx.coroutines.*
import pt.ulusofona.deisi.a2020.cm.g15.data.remote.requests.County
import pt.ulusofona.deisi.a2020.cm.g15.data.remote.services.Last24hService
import pt.ulusofona.deisi.a2020.cm.g15.data.room.dao.CountiesDao
import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.CountyEnt
import retrofit2.Retrofit
import retrofit2.awaitResponse

private val TAG: String = CountiesRepository::class.java.simpleName


class CountiesRepository(private val retrofit: Retrofit, private val countiesDao: CountiesDao) {

    private var countyTemp = County("0","ABRANTES","0",0,"0",0,0)
    private var counties = MutableList(1){countyTemp} as ArrayList<County>

    private var countiesEnt = mutableListOf<CountyEnt>()

    fun getCountiesOperations(internet:Boolean):MutableList<CountyEnt>{
        return if (internet){
            updateCountiesApi()
            getCachedCounties()
        }else{
            getCachedCounties()
        }
    }


    private fun updateCountiesApi(){
        val service = retrofit.create(Last24hService::class.java)

        CoroutineScope(Dispatchers.IO).async {
            //Log.i(TAG,"Waiting Response")
            val request = service.getLastUpdateCounties().awaitResponse()
            //Log.i(TAG,"Received")
            if (request.isSuccessful){
                //Log.i(TAG,"Successful")
                counties = request.body()!!

                /* Serve para poupar recursos caso não tenha havido uma nova atualização
                val abrantesData = countiesDao.getByCounty("ABRANTES").date
                if(countyTemp.data != counties[0].data){*/
                    for(i in counties){
                        val newCounty = CountyEnt(
                            i.concelho,
                            i.distrito,
                            i.data,
                            i.incidencia,
                            i.incidenciaRisco,
                            i.casos14dias,
                            i.population
                        )
                        countiesDao.insert(newCounty)
                    }
                /*}else{
                    Log.i(TAG,"Não houve novas atualizações dos concelhos")
                }*/
            } else {
                Log.i(TAG,"UnSuccessful")
            }
        }
    }

    fun getCachedCounties():MutableList<CountyEnt>{
        GlobalScope.launch(Dispatchers.IO) {
            countiesEnt = countiesDao.getAllCounties()
        }
        Thread.sleep(50)
        return countiesEnt
    }

    fun getSearchCounties(search: String): MutableList<CountyEnt> {
        GlobalScope.launch(Dispatchers.IO) {
            countiesEnt = countiesDao.getByCounty(search)
        }
        Thread.sleep(50)
        return countiesEnt
    }

    fun getDangerLevelCounties(dangerLevel: String): MutableList<CountyEnt> {
        GlobalScope.launch(Dispatchers.IO) {
            countiesEnt = countiesDao.getByDangerLevel(dangerLevel)
        }
        Thread.sleep(50)
        return countiesEnt
    }

    fun getIntervalCounties(min: Int,max: Int): MutableList<CountyEnt> {
        GlobalScope.launch(Dispatchers.IO) {
            countiesEnt = countiesDao.getByInterval(min,max)
        }
        Thread.sleep(50)
        return countiesEnt
    }

    fun getCustomCounties(search: String,dangerLevel: String,min: Int,max: Int): MutableCollection<CountyEnt> {
        GlobalScope.launch(Dispatchers.IO) {
            countiesEnt = countiesDao.getCustomCounties(search, dangerLevel, min, max)
        }
        Thread.sleep(50)
        return countiesEnt
    }



}