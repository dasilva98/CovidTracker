package pt.ulusofona.deisi.a2020.cm.g15.domain.tests

import android.app.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.a2020.cm.g15.data.repositories.TestsRepository
import pt.ulusofona.deisi.a2020.cm.g15.data.room.TestsDatabase
import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.TestEnt

class TestsLogic(application: Application) {

    val repository: TestsRepository
    private var asc = true


    init {
        val testsDao = TestsDatabase.getInstance(application).testDao()
        repository = TestsRepository(testsDao)
    }

    fun insertTest(testEnt : TestEnt){
        GlobalScope.launch(Dispatchers.IO) {
            repository.addTest(testEnt)
        }
    }

    fun getSortedTests():Boolean{
        asc = !asc
        repository.getDataSorted(asc)
        return asc
    }

}