package pt.ulusofona.deisi.a2020.cm.g15.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pt.ulusofona.deisi.a2020.cm.g15.data.room.dao.VaccinesDao
import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.VaccineEnt

@Database(entities = [VaccineEnt::class],version = 1,exportSchema = false)
abstract class VaccinesDatabase:RoomDatabase() {
    abstract fun vaccineDao(): VaccinesDao

    companion object{
        private var instance: VaccinesDatabase? = null
        fun getInstance(applicationContext: Context):VaccinesDatabase{
            synchronized(this){
                if (instance == null){
                    instance = Room.databaseBuilder(
                        applicationContext,
                        VaccinesDatabase::class.java,
                        "vaccines_db"
                    ).build()
                }
                return instance as VaccinesDatabase
            }
        }
    }
}