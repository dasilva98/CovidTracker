package pt.ulusofona.deisi.a2020.cm.g15.ui.viewmodels

import android.app.Application
import android.widget.ListAdapter
import androidx.lifecycle.AndroidViewModel
import pt.ulusofona.deisi.a2020.cm.g15.domain.tests.TestsLogic
import pt.ulusofona.deisi.a2020.cm.g15.ui.adapters.TestAdapter

class TestsViewModel(application: Application) : AndroidViewModel(application){

    var testsLogic = TestsLogic(application)

    var adapter = TestAdapter()

    fun setAdapter(){
        adapter.setData(testsLogic.repository.readAllData)
    }

    fun onClickSortDate():Boolean {
        return testsLogic.getSortedTests()
    }

}