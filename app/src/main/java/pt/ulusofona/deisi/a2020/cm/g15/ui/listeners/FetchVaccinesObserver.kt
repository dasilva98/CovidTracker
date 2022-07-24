package pt.ulusofona.deisi.a2020.cm.g15.ui.listeners

interface FetchVaccinesObserver {
    fun onVaccinesFetched(
        vacinadosTotal: String,
        primeiraInoculacaoNumero: String,
        primeiraInoculacaoPercentagem: String,
        segundaInoculacaoNumero: String,
        segundaInoculacaoPercentagem: String
    )
}