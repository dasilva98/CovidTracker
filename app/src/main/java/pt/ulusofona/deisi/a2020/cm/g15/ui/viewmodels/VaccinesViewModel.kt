package pt.ulusofona.deisi.a2020.cm.g15.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import pt.ulusofona.deisi.a2020.cm.g15.data.remote.RetrofitBuilder
import pt.ulusofona.deisi.a2020.cm.g15.data.repositories.VaccineRepository
import pt.ulusofona.deisi.a2020.cm.g15.data.room.VaccinesDatabase
import pt.ulusofona.deisi.a2020.cm.g15.domain.dashboard.VaccinesLogic
import pt.ulusofona.deisi.a2020.cm.g15.ui.listeners.FetchVaccinesObserver
import pt.ulusofona.deisi.a2020.cm.g15.ui.listeners.ReceiveVaccinesObserver

private const val ENDPOINT = "https://www.vacinacaocovid19.pt/api/"

class VaccinesViewModel(application: Application): AndroidViewModel(application), FetchVaccinesObserver {

    private val vaccineRepository = VaccineRepository(
        RetrofitBuilder.getInstance(ENDPOINT),
        VaccinesDatabase.getInstance(application).vaccineDao()
    )

    private val vaccinesLogic = VaccinesLogic(vaccineRepository)

    fun getVaccines(internet: Boolean){
        vaccinesLogic.getVaccines(this,internet)
    }

    override fun onVaccinesFetched(
        vacinadosTotal: String,
        primeiraInoculacaoNumero: String,
        primeiraInoculacaoPercentagem: String,
        segundaInoculacaoNumero: String,
        segundaInoculacaoPercentagem: String
    ) {
        notifyVaccinesObserver(
            vacinadosTotal,
            primeiraInoculacaoNumero,
            primeiraInoculacaoPercentagem,
            segundaInoculacaoNumero,
            segundaInoculacaoPercentagem
        )
    }

    companion object{
        private var observer: ReceiveVaccinesObserver? = null

        fun registerVaccineObserver(observer: ReceiveVaccinesObserver){
            this.observer = observer
        }

        fun unregisterObserver(){
            observer = null
        }

        private fun notifyVaccinesObserver(
            vacinadosTotal:String,
            primeiraInoculacaoNumero:String,
            primeiraInoculacaoPercentagem:String,
            segundaInoculacaoNumero:String,
            segundaInoculacaoPercentagem:String
        ){
            observer?.onReceiveVaccine(
                vacinadosTotal,
                primeiraInoculacaoNumero,
                primeiraInoculacaoPercentagem,
                segundaInoculacaoNumero,
                segundaInoculacaoPercentagem
            )
        }

    }

}