package pt.ulusofona.deisi.a2020.cm.g15.ui.listeners

interface ReceiveLast24hObserver {
    fun onLast24hChanged(
        confirmados: String?,
        recuperados: String?,
        obitos: String?,
        internados: String?,
        internadosUci: String?
    )
}