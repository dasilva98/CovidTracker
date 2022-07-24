package pt.ulusofona.deisi.a2020.cm.g15.ui.viewmodels

import androidx.lifecycle.ViewModel
import pt.ulusofona.deisi.a2020.cm.g15.data.remote.RetrofitBuilder
import pt.ulusofona.deisi.a2020.cm.g15.domain.InDangerLogic
import pt.ulusofona.deisi.a2020.cm.g15.ui.listeners.FetchDistrictObserver
import pt.ulusofona.deisi.a2020.cm.g15.ui.listeners.ReceiveDistrictObserver

private const val ENDPOINT = "https://covid19-api.vost.pt/Requests/"

class InDangerViewModel(): ViewModel(),FetchDistrictObserver {

    private val inDangerLogic = InDangerLogic(RetrofitBuilder.getInstance(ENDPOINT))

    fun getInDanger(internet : Boolean){
        inDangerLogic.getInDangerLevel(this,internet)
    }

    fun districtChanged(district: String,internet: Boolean){
        inDangerLogic.setDistrict(this,district,internet)
    }

    override fun onDistrictFetched(dangerLevel: String){
        notifyDistrictObserver(dangerLevel)
    }

    companion object{
        private var observer: ReceiveDistrictObserver? = null

        fun registerDistrictObserver(observer: ReceiveDistrictObserver){
            this.observer = observer
        }

        fun unregisterObserver(){
            observer = null
        }

        private fun notifyDistrictObserver(dangerLevel: String) {
            observer?.onDistrictChanged(dangerLevel)
        }
    }
}