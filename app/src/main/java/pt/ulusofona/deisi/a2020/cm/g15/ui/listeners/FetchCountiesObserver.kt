package pt.ulusofona.deisi.a2020.cm.g15.ui.listeners

import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.CountyEnt

interface FetchCountiesObserver {
    fun onCountiesFetched(
        counties: MutableList<CountyEnt>
    )
}