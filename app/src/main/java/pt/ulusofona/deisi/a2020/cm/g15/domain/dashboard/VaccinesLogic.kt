package pt.ulusofona.deisi.a2020.cm.g15.domain.dashboard

import android.util.Log
import pt.ulusofona.deisi.a2020.cm.g15.data.repositories.VaccineRepository
import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.VaccineEnt
import pt.ulusofona.deisi.a2020.cm.g15.ui.listeners.FetchVaccinesObserver
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

private val TAG: String = VaccinesLogic::class.java.simpleName

class VaccinesLogic(private val repository: VaccineRepository) {

    private var populTotal : Float = 9800494F
    private var vaccine = VaccineEnt(0,0,0,0,0)

    fun getVaccines(callback: FetchVaccinesObserver,internet: Boolean){
        extractVaccineApi(internet)
        Thread.sleep(50)
        callback.onVaccinesFetched(
            vacinasTotais(),
            primeiraInocolacaoNumero(),
            primeiraInocolacaoPercentagem(),
            segundaInocolacaoNumero(),
            segundaInocolacaoPercentagem()
        )
    }

    fun extractVaccineApi(internet: Boolean) {
        vaccine = repository.getVaccineOperations(internet)
    }

    fun vacinasTotais(): String{
        return DecimalFormat("###,###", DecimalFormatSymbols().apply {
            groupingSeparator = ' '
        }).format(vaccine.vacinadosAc).toString()
    }

    fun primeiraInocolacaoNumero(): String{
        return DecimalFormat("###,###", DecimalFormatSymbols().apply {
            groupingSeparator = ' '
        }).format(vaccine.inoculacao1Ac).toString()
    }

    fun primeiraInocolacaoPercentagem(): String{
        var resultado = vaccine.inoculacao1Ac.toFloat()
        resultado = (resultado / populTotal) * 100
        return "%.2f".format(resultado) + "%"
    }

    fun segundaInocolacaoNumero(): String {
        return DecimalFormat("###,###", DecimalFormatSymbols().apply {
            groupingSeparator = ' '
        }).format(vaccine.inoculacao2Ac).toString()
    }

    fun segundaInocolacaoPercentagem(): String {
        var resultado = vaccine.inoculacao2Ac.toFloat()
        resultado = (resultado / populTotal) * 100
        return "%.2f".format(resultado) + "%"
    }
}