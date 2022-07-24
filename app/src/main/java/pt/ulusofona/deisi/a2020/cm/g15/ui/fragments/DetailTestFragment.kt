package pt.ulusofona.deisi.a2020.cm.g15.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_detail_test.*
import pt.ulusofona.deisi.a2020.cm.g15.R
import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.TestEnt
import pt.ulusofona.deisi.a2020.cm.g15.ui.viewmodels.TestsViewModel

class DetailTestFragment : Fragment(R.layout.fragment_detail_test) {

    private val args: DetailTestFragmentArgs by navArgs()
    private lateinit var viewModel : TestsViewModel
    private lateinit var testEnt: TestEnt

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_test,container,false)
        viewModel = ViewModelProvider(this).get(TestsViewModel::class.java)
        testEnt = viewModel.testsLogic.repository.getById(args.id)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageResult.setImageResource(testEnt.testImage)
        result.text = if (testEnt.result) "Positivo" else "Negativo"
        local.text = testEnt.location
        date.text = testEnt.date
    }

}