package pt.ulusofona.deisi.a2020.cm.g15.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.a2020.cm.g15.data.room.dao.TestsDao
import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.TestEnt

class TestsRepository(private val testsDao:TestsDao) {

    var readAllData: List<TestEnt> = mutableListOf()

    lateinit var testEnt: TestEnt

    init {
        getData()
    }

    suspend fun addTest(testEnt: TestEnt){
        testsDao.insert(testEnt)
    }

    fun getData(): List<TestEnt>{
        GlobalScope.launch(Dispatchers.IO) {
            readAllData = testsDao.getAllAsc()
        }
        return readAllData
    }

    fun getById(id:String):TestEnt{
        GlobalScope.launch(Dispatchers.IO) {
            testEnt = testsDao.getById(id)
        }
        Thread.sleep(50)
        return testEnt
    }

    fun getDataSorted(asc: Boolean): List<TestEnt>{
        GlobalScope.launch(Dispatchers.IO) {
            readAllData = if(asc){
                testsDao.getAllAsc()
            }else{
                testsDao.getAllDsc()
            }

        }
        Thread.sleep(50)
        return readAllData
    }

}