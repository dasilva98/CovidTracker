package pt.ulusofona.deisi.a2020.cm.g15.domain.dashboard


import android.util.Log
import pt.ulusofona.deisi.a2020.cm.g15.data.repositories.CountiesRepository
import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.CountyEnt
import pt.ulusofona.deisi.a2020.cm.g15.ui.listeners.FetchCountiesObserver
import java.util.*

private val TAG: String = CountiesLogic::class.java.simpleName

class CountiesLogic(private val repository: CountiesRepository) {

    fun getCounties(callback: FetchCountiesObserver,internet:Boolean){

        Thread.sleep(50)
        callback.onCountiesFetched(
            extractCountyApi(internet)
        )
    }

    private fun extractCountyApi(internet: Boolean): MutableList<CountyEnt>{
        return repository.getCountiesOperations(internet)
    }

    fun getFilteredCounties(callback: FetchCountiesObserver,search: String, dangerLevel: String, minInterval: String, maxInterval: String){
        callback.onCountiesFetched(
            selectQuery(search,dangerLevel,minInterval,maxInterval)
        )
    }

    private fun selectQuery(search: String, dangerLevel: String, minInterval: String, maxInterval: String): MutableList<CountyEnt>{
        return if (search != ""){
            repository.getSearchCounties(search.toUpperCase(Locale.ROOT))
        } else if (dangerLevel != "" && dangerLevel!= "Every Level" && dangerLevel!= "Todos os Graus"){
            var changedLevel = ""
            changedLevel = if (dangerLevel == "Low to Moderate") {
                "Baixo a Moderado"
            }else if (dangerLevel == "Moderate"){
                "Moderado"
            }else if (dangerLevel == "High"){
                "Elevado"
            }else if (dangerLevel == "Very High"){
                "Muito Elevado"
            }else if(dangerLevel == "Extremely High"){
                "Extremamente Elevado"
            }else{
                dangerLevel
            }
            repository.getDangerLevelCounties(changedLevel)
        }else if(minInterval != "" && maxInterval != ""){
            val min = minInterval.toInt()
            val max = maxInterval.toInt()
            repository.getIntervalCounties(min,max)
        } else {
            repository.getCachedCounties()
        }
    }
}