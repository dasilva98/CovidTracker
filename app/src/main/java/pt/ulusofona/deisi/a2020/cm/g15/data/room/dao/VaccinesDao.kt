package pt.ulusofona.deisi.a2020.cm.g15.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.VaccineEnt

@Dao
interface VaccinesDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(vaccineEnt: VaccineEnt)

    @Query("SELECT * FROM vaccineent WHERE id = :vaccineId")
    suspend fun getById(vaccineId: Int): VaccineEnt

    @Query("SELECT * FROM vaccineent")
    suspend fun getAllVaccines():MutableList<VaccineEnt>
}
