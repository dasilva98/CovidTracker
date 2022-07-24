package pt.ulusofona.deisi.a2020.cm.g15.ui.listeners

interface FetchDistrictObserver {
    fun onDistrictFetched(
        dangerLevel: String
    )
}