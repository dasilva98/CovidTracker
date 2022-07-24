package pt.ulusofona.deisi.a2020.cm.g15.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.TestEnt

@Dao
interface TestsDao {

    @Insert
    suspend fun insert(testEnt: TestEnt)

    @Query("SELECT * FROM testent")
    suspend fun getAll(): List<TestEnt>

    @Query("SELECT * FROM testent WHERE uuid =:uuid")
    suspend fun getById(uuid: String): TestEnt

    @Query("SELECT * FROM testent ORDER BY date ASC")
    suspend fun getAllAsc(): List<TestEnt>

    @Query("SELECT * FROM testent ORDER BY date DESC")
    suspend fun getAllDsc(): List<TestEnt>
}