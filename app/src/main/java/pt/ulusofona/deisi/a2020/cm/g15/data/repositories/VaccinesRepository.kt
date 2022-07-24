package pt.ulusofona.deisi.a2020.cm.g15.data.repositories

import android.util.Log
import kotlinx.coroutines.*
import pt.ulusofona.deisi.a2020.cm.g15.data.remote.requests.Vaccine
import pt.ulusofona.deisi.a2020.cm.g15.data.remote.services.VaccinesService
import pt.ulusofona.deisi.a2020.cm.g15.data.room.dao.VaccinesDao
import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.VaccineEnt
import retrofit2.Retrofit
import retrofit2.awaitResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private val TAG: String = VaccineRepository::class.java.simpleName

class VaccineRepository(private val retrofit: Retrofit, private val vaccinesDao: VaccinesDao) {
    private var vaccineTemp = Vaccine(1,2,3,4)
    var vaccines = MutableList(1){vaccineTemp} as ArrayList<Vaccine>

    private var vaccineEnt = VaccineEnt(0,0,0,0,0)

    fun getVaccineOperations(internet: Boolean) : VaccineEnt{
        return if (internet) {
            updateVaccineApi()
            getCachedVaccine()
        }else{
            getCachedVaccine()
        }
    }

    private fun updateVaccineApi(){
        val service = retrofit.create(VaccinesService::class.java)

        CoroutineScope(Dispatchers.IO).async {
            //Log.i(TAG,"Waiting Response")
            val request = service.getVaccines().awaitResponse()

            if (request.isSuccessful){
                //Log.i(TAG,"Successful")
                vaccines = request.body()!!
                val newVaccine = VaccineEnt(
                    0,
                    vaccines.last().data,
                    vaccines.last().inoculacao1Ac,
                    vaccines.last().inoculacao2Ac,
                    vaccines.last().vacinadosAc
                )
                vaccinesDao.insert(newVaccine)
            } else{
                Log.i(TAG,"UnSuccessful")
            }
        }
    }

    private fun getCachedVaccine():VaccineEnt{
        GlobalScope.launch(Dispatchers.IO) {
            val vacList = vaccinesDao.getAllVaccines()
            if (vacList.isEmpty()){
                vaccineEnt = VaccineEnt(0,0,0,0,0)
            }else{
                vaccineEnt = vaccinesDao.getById(0)
            }
        }
        Thread.sleep(50)
        return vaccineEnt
    }
}