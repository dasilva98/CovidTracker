package pt.ulusofona.deisi.a2020.cm.g15.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.LastUpdateEnt

@Dao
interface Last24hDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(lastUpdateEnt: LastUpdateEnt)

    @Query("SELECT * FROM lastupdateent WHERE id = :lastupdateId")
    suspend fun getById(lastupdateId: Int): LastUpdateEnt

    @Query("SELECT * FROM lastupdateent")
    suspend fun getAll():MutableList<LastUpdateEnt>
}