package pt.ulusofona.deisi.a2020.cm.g15.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pt.ulusofona.deisi.a2020.cm.g15.data.room.dao.CountiesDao
import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.CountyEnt

@Database(entities = [CountyEnt::class],version = 1,exportSchema = false)
abstract class CountiesDatabase:RoomDatabase() {
    abstract fun countiesDao():CountiesDao

    companion object{
        private var instance: CountiesDatabase? = null

        fun getInstance(applicationContext: Context):CountiesDatabase{
            synchronized(this){
                if(instance == null){
                    instance = Room.databaseBuilder(
                        applicationContext,
                        CountiesDatabase::class.java,
                        "counties_db"
                    ).build()
                }
                return instance as CountiesDatabase
            }
        }
    }
}