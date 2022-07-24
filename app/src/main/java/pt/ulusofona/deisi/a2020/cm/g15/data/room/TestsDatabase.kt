package pt.ulusofona.deisi.a2020.cm.g15.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pt.ulusofona.deisi.a2020.cm.g15.data.room.dao.TestsDao
import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.TestEnt

@Database(entities = [TestEnt::class], version = 1, exportSchema = false)
abstract class TestsDatabase:RoomDatabase() {
    abstract fun testDao():TestsDao

    companion object{
        private var instance: TestsDatabase? = null
        fun getInstance(applicationContext: Context): TestsDatabase{
            synchronized(this){
                if (instance == null){
                    instance = Room.databaseBuilder(
                        applicationContext,
                        TestsDatabase::class.java,
                        "tests_db"
                    ).build()
                }
                return instance as TestsDatabase
            }
        }
    }
}