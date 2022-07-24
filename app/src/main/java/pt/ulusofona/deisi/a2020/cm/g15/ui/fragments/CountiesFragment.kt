package pt.ulusofona.deisi.a2020.cm.g15.ui.fragments

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Optional
import kotlinx.android.synthetic.main.fragment_counties.*
import kotlinx.android.synthetic.main.fragment_counties.view.*
import pt.ulusofona.deisi.a2020.cm.g15.R
import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.CountyEnt
import pt.ulusofona.deisi.a2020.cm.g15.ui.adapters.CountyAdapter
import pt.ulusofona.deisi.a2020.cm.g15.ui.listeners.ReceiveCountiesObserver
import pt.ulusofona.deisi.a2020.cm.g15.ui.viewmodels.CountiesViewModel

class CountiesFragment : Fragment(R.layout.fragment_counties),ReceiveCountiesObserver {

    private lateinit var viewModel: CountiesViewModel

    private var adapter = CountyAdapter()

    private val ADAPTER_KEY = "counties"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_counties, container, false)
        viewModel = ViewModelProviders.of(this).get(CountiesViewModel::class.java)

        //ButterKnife
        ButterKnife.bind(this,view)

        //Autocomplete Dropdown
        val textViewSearch = view.findViewById<AutoCompleteTextView>(R.id.autocomplete_counties)

        val counties: Array<out String> = resources.getStringArray(R.array.counties_array)
        ArrayAdapter<String>(activity as Context,android.R.layout.simple_list_item_1,counties).also {
                adapter -> textViewSearch.setAdapter(adapter)
        }

        val textViewDangerLevels = view.findViewById<AutoCompleteTextView>(R.id.risk_level)
        val dangerLevels = resources.getStringArray(R.array.danger_levels_dropdown)
        ArrayAdapter<String>(activity as Context, R.layout.item_dropdown_dangerlevel,dangerLevels).also {
            adapter -> textViewDangerLevels.setAdapter(adapter)
        }

        //Search and Filters
        view.search_button.setOnClickListener {
            viewModel.onClickSearch(
                autocomplete_counties.text.toString(),
                risk_level.text.toString(),
                min_dangerlevel.text.toString(),
                max_dangerlevel.text.toString()
            )
        }


        //Recyclerview
        val recyclerView = view.list_recyclerview_counties
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity as Context)

        return view
    }

    override fun onStart() {
        CountiesViewModel.registerCountiesObserver(this)
        viewModel.getCounties(internetConnection())
        super.onStart()
    }

    fun internetConnection(): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    override fun onCountiesChanged(counties: MutableList<CountyEnt>) {
        adapter.setData(counties)
    }

    override fun onDestroy() {
        CountiesViewModel.unregisterObserver()
        super.onDestroy()
    }
}