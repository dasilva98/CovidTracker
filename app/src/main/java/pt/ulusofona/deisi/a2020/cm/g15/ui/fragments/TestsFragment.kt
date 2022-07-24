package pt.ulusofona.deisi.a2020.cm.g15.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_tests.*
import kotlinx.android.synthetic.main.fragment_tests.view.*
import pt.ulusofona.deisi.a2020.cm.g15.R
import pt.ulusofona.deisi.a2020.cm.g15.ui.viewmodels.TestsViewModel


class TestsFragment : Fragment(R.layout.fragment_tests) {

    private lateinit var testsViewModel: TestsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tests,container,false)

        testsViewModel = ViewModelProvider(this).get(TestsViewModel::class.java)

        // Recyclerview
        val recyclerView = view.list_recyclerview_tests
        recyclerView.adapter = testsViewModel.adapter
        recyclerView.layoutManager = LinearLayoutManager(activity as Context)
        testsViewModel.setAdapter()

        view.addTestButton.setOnClickListener {
            val action = TestsFragmentDirections.actionTestsFragmentToFormTestFragment()
            findNavController().navigate(action)
        }



        view.sortDate.setOnClickListener {
            val asc = testsViewModel.onClickSortDate()
            testsViewModel.setAdapter()
            if(asc) {
                iconSortDate.scaleY = (1).toFloat()
            } else {
                iconSortDate.scaleY = (-1).toFloat()
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        testsViewModel.adapter.setData(testsViewModel.testsLogic.repository.readAllData)
    }
}