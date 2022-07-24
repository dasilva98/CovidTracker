package pt.ulusofona.deisi.a2020.cm.g15.ui.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_vaccines.*
import pt.ulusofona.deisi.a2020.cm.g15.R
import pt.ulusofona.deisi.a2020.cm.g15.data.repositories.Last24hRepository
import pt.ulusofona.deisi.a2020.cm.g15.ui.listeners.ReceiveVaccinesObserver
import pt.ulusofona.deisi.a2020.cm.g15.ui.viewmodels.VaccinesViewModel

class VaccinesFragment:Fragment(R.layout.fragment_vaccines),ReceiveVaccinesObserver {

    private lateinit var viewModel : VaccinesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vaccines,container,false)
        viewModel = ViewModelProviders.of(this).get(VaccinesViewModel::class.java)
        return view
    }

    override fun onStart() {
        VaccinesViewModel.registerVaccineObserver(this)
        viewModel.getVaccines(internetConnection())
        super.onStart()
    }

    fun internetConnection(): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    override fun onReceiveVaccine(
        vaccinadosTotal: String?,
        primeiraInoculacaoNumero: String?,
        primeiraInoculacaoPercentagem: String?,
        segundaInoculacaoNumero: String?,
        segundaInoculacaoPercentagem: String?
    ) {
        vaccinadosTotal.let { doses_administradas_numero.text = it }
        primeiraInoculacaoNumero.let { first_ino_number.text = it }
        primeiraInoculacaoPercentagem.let { first_ino_percentage.text = it }
        segundaInoculacaoNumero.let { second_ino_number.text = it }
        segundaInoculacaoPercentagem.let { second_ino_percentage.text = it }
    }

    override fun onDestroy() {
        VaccinesViewModel.unregisterObserver()
        super.onDestroy()
    }

}