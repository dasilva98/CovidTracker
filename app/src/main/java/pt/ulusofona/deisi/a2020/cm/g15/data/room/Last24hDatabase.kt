package pt.ulusofona.deisi.a2020.cm.g15.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pt.ulusofona.deisi.a2020.cm.g15.data.room.dao.Last24hDao
import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.LastUpdateEnt

@Database(entities = [LastUpdateEnt::class],version = 1,exportSchema = false)
abstract class Last24hDatabase:RoomDatabase() {
    abstract fun last24hDao():Last24hDao

    companion object{
        private var instance: Last24hDatabase? = null
        fun getInstance(applicationContext: Context):Last24hDatabase{
            synchronized(this){
                if (instance==null){
                    instance = Room.databaseBuilder(
                        applicationContext,
                        Last24hDatabase::class.java,
                        "last24h_db"
                    ).build()
                }
                return instance as Last24hDatabase
            }
        }
    }
}