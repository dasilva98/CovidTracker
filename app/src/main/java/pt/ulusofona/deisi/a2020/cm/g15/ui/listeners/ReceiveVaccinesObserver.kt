package pt.ulusofona.deisi.a2020.cm.g15.ui.listeners

interface ReceiveVaccinesObserver {
    fun onReceiveVaccine(
        vaccinadosTotal: String?,
        primeiraInoculacaoNumero: String?,
        primeiraInoculacaoPercentagem: String?,
        segundaInoculacaoNumero: String?,
        segundaInoculacaoPercentagem: String?
    )
}