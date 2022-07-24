package pt.ulusofona.deisi.a2020.cm.g15.ui.listeners

interface FetchLast24hObserver {
    fun onLast24hFetched(
        confirmados: String,
        recuperados: String,
        obitos: String,
        internados: String,
        internadosUci: String
    )
}