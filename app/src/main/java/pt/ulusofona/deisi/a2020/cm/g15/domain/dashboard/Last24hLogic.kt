package pt.ulusofona.deisi.a2020.cm.g15.domain.dashboard

import android.util.Log
import pt.ulusofona.deisi.a2020.cm.g15.data.repositories.Last24hRepository
import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.LastUpdateEnt
import pt.ulusofona.deisi.a2020.cm.g15.ui.listeners.FetchLast24hObserver
import java.text.SimpleDateFormat
import java.util.*

private val TAG: String = Last24hLogic::class.java.simpleName

class Last24hLogic( private val repository: Last24hRepository) {

    private var todaylast24h = LastUpdateEnt(0,"0",0,0,0,0,0)
    private var yesterdaylast24h = LastUpdateEnt(0,"0",0,0,0,0,0)

    fun getLast24h(callback: FetchLast24hObserver,internet: Boolean){
        extractLast24hApi(internet)
        Thread.sleep(50)
        callback.onLast24hFetched(
            confirmadosCalc(),
            recuperadosCalc(),
            obitosCalc(),
            internadosCalc(),
            internadosUciCalc()
        )
    }

    fun extractLast24hApi(internet: Boolean){
        val dates = calculateDay()
        val pair24h : Pair<LastUpdateEnt,LastUpdateEnt> = repository.getLast24hOperations(internet,dates.first,dates.second)
        todaylast24h = pair24h.first
        yesterdaylast24h = pair24h.second
    }

    fun calculateDay():Pair<String,String>{
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE,-1)
        val today = dateFormat.format(calendar.time)
        calendar.add(Calendar.DATE,-1)
        val yesterday = dateFormat.format(calendar.time)
        return Pair(today,yesterday)
    }

    fun confirmadosCalc(): String {
        val hoje = todaylast24h.confirmadosNovos
        return if (hoje >= 0){
            "+$hoje"
        }else{
            hoje.toString()
        }
    }

    fun recuperadosCalc(): String {
        val hoje = todaylast24h.recuperados
        val ontem = yesterdaylast24h.recuperados
        val recuperadosDif = hoje - ontem

        return if (recuperadosDif >= 0){
            "+$recuperadosDif"
        }else{
            recuperadosDif.toString()
        }
    }

    fun obitosCalc(): String {
        val hoje = todaylast24h.obitos
        val ontem = yesterdaylast24h.obitos
        val obitosDif = hoje - ontem

        return if (obitosDif >= 0){
            "+$obitosDif"
        }else{
            obitosDif.toString()
        }
    }

    fun internadosCalc(): String {
        val hoje = todaylast24h.internados
        val ontem = yesterdaylast24h.internados
        val internadosDif = hoje - ontem

        return if (internadosDif >= 0){
            "+$internadosDif"
        }else{
            internadosDif.toString()
        }
    }

    fun internadosUciCalc(): String {
        val hoje = todaylast24h.internadosUci
        val ontem = yesterdaylast24h.internadosUci
        val internadosUciDif = hoje - ontem

        return if (internadosUciDif >= 0){
            "+$internadosUciDif"
        }else{
            internadosUciDif.toString()
        }
    }
}