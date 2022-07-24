package pt.ulusofona.deisi.a2020.cm.g15.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import pt.ulusofona.deisi.a2020.cm.g15.data.remote.RetrofitBuilder
import pt.ulusofona.deisi.a2020.cm.g15.data.repositories.Last24hRepository
import pt.ulusofona.deisi.a2020.cm.g15.data.room.Last24hDatabase
import pt.ulusofona.deisi.a2020.cm.g15.domain.dashboard.Last24hLogic
import pt.ulusofona.deisi.a2020.cm.g15.ui.listeners.FetchLast24hObserver
import pt.ulusofona.deisi.a2020.cm.g15.ui.listeners.ReceiveLast24hObserver

private const val ENDPOINT = "https://covid19-api.vost.pt/Requests/"

class Last24hViewModel(application: Application): AndroidViewModel(application),FetchLast24hObserver {

    private val last24hRepository = Last24hRepository(
        RetrofitBuilder.getInstance(ENDPOINT),
        Last24hDatabase.getInstance(application).last24hDao()
    )

    private val last24hLogic = Last24hLogic(last24hRepository)

    fun getLast24h(internet: Boolean){
        last24hLogic.getLast24h(this,internet)
    }

    override fun onLast24hFetched(
        confirmados: String,
        recuperados: String,
        obitos: String,
        internados: String,
        internadosUci: String
    ) {
        notifyLast24hObserver(
            confirmados,
            recuperados,
            obitos,
            internados,
            internadosUci
        )
    }

    companion object{
        private var observer: ReceiveLast24hObserver? = null

        fun registerLast24hObserver(observer: ReceiveLast24hObserver){
            this.observer = observer
        }

        fun unregisterObserver(){
            observer = null
        }

        private fun notifyLast24hObserver(
            confirmados: String,
            recuperados: String,
            obitos: String,
            internados: String,
            internadosUci: String
        ){
            observer?.onLast24hChanged(
                confirmados,
                recuperados,
                obitos,
                internados,
                internadosUci
            )
        }
    }
}