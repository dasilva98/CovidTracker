package pt.ulusofona.deisi.a2020.cm.g15.ui.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_last24h.*
import pt.ulusofona.deisi.a2020.cm.g15.R
import pt.ulusofona.deisi.a2020.cm.g15.ui.listeners.ReceiveLast24hObserver
import pt.ulusofona.deisi.a2020.cm.g15.ui.viewmodels.Last24hViewModel

class Last24hFragment: Fragment(R.layout.fragment_last24h),ReceiveLast24hObserver{

    private lateinit var viewModel : Last24hViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_last24h,container,false)
        viewModel = ViewModelProviders.of(this).get(Last24hViewModel::class.java)
        return view
    }

    override fun onStart() {
        Last24hViewModel.registerLast24hObserver(this)
        viewModel.getLast24h(internetConnection())
        super.onStart()
    }

    fun internetConnection(): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    override fun onLast24hChanged(
        confirmados: String?,
        recuperados: String?,
        obitos: String?,
        internados: String?,
        internadosUci: String?
    ) {
        confirmados.let { casos_confirmados_numero.text = it}
        recuperados.let { recuperados_numero.text = it}
        obitos.let { obitos_numero.text = it}
        internados.let { internados_numero.text = it}
        internadosUci.let { internados_uci_numero.text = it}
    }

    override fun onDestroy() {
        Last24hViewModel.unregisterObserver()
        super.onDestroy()
    }

}