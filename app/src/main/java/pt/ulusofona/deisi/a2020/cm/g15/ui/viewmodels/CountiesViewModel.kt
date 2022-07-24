package pt.ulusofona.deisi.a2020.cm.g15.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import pt.ulusofona.deisi.a2020.cm.g15.data.remote.RetrofitBuilder
import pt.ulusofona.deisi.a2020.cm.g15.data.repositories.CountiesRepository
import pt.ulusofona.deisi.a2020.cm.g15.data.room.CountiesDatabase
import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.CountyEnt
import pt.ulusofona.deisi.a2020.cm.g15.domain.dashboard.CountiesLogic
import pt.ulusofona.deisi.a2020.cm.g15.ui.adapters.CountyAdapter
import pt.ulusofona.deisi.a2020.cm.g15.ui.listeners.FetchCountiesObserver
import pt.ulusofona.deisi.a2020.cm.g15.ui.listeners.ReceiveCountiesObserver

private const val ENDPOINT = "https://covid19-api.vost.pt/Requests/"

class CountiesViewModel(application: Application) : AndroidViewModel(application),FetchCountiesObserver {

    private val countiesRepository = CountiesRepository(
        RetrofitBuilder.getInstance(ENDPOINT),
        CountiesDatabase.getInstance(application).countiesDao()
    )

    private val countiesLogic = CountiesLogic(countiesRepository)

    fun getCounties(internet:Boolean){
        countiesLogic.getCounties(this,internet)
    }

    override fun onCountiesFetched(counties: MutableList<CountyEnt>) {
        notifyCountiesObserver(counties)
    }

    fun onClickSearch(search: String, dangerLevel: String, minInterval: String, maxInterval: String){
        countiesLogic.getFilteredCounties(this,search,dangerLevel,minInterval,maxInterval)
    }

    companion object{
        private var observer: ReceiveCountiesObserver? = null

        fun registerCountiesObserver(observer: ReceiveCountiesObserver){
            this.observer = observer
        }

        fun unregisterObserver(){
            observer = null
        }

        private fun notifyCountiesObserver(counties: MutableList<CountyEnt>){
            observer?.onCountiesChanged(counties)
        }
    }
}