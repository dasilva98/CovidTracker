package pt.ulusofona.deisi.a2020.cm.g15.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import pt.ulusofona.deisi.a2020.cm.g15.data.repositories.TestsRepository
import pt.ulusofona.deisi.a2020.cm.g15.data.room.TestsDatabase
import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.TestEnt
import pt.ulusofona.deisi.a2020.cm.g15.domain.tests.TestsLogic

class TestViewModel(application: Application):AndroidViewModel(application) {

    private val repository: TestsRepository

    init {
        val testDao = TestsDatabase.getInstance(application).testDao()
        repository = TestsRepository(testDao)
    }

    private val testsLogic = TestsLogic(application)

    fun addTest(testEnt: TestEnt){
        testsLogic.insertTest(testEnt)
    }

}