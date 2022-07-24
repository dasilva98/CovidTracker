package pt.ulusofona.deisi.a2020.cm.g15.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.CountyEnt

@Dao
interface CountiesDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(countyEnt: CountyEnt)

    @Query("SELECT * FROM countyent WHERE county = :county")
    suspend fun getByCounty(county: String):MutableList<CountyEnt>

    @Query("SELECT * FROM countyent")
    suspend fun getAllCounties():MutableList<CountyEnt>

    @Query("SELECT * FROM countyent WHERE riskLevel = :dangerLevel")
    suspend fun getByDangerLevel(dangerLevel: String):MutableList<CountyEnt>

    @Query("SELECT * FROM countyent WHERE incidence BETWEEN :min AND :max")
    suspend fun getByInterval(min: Int,max: Int):MutableList<CountyEnt>

    @Query("SELECT * FROM countyent WHERE county = :search AND (riskLevel = :dangerLevel OR incidence BETWEEN :min AND :max)")
    suspend fun getCustomCounties(search: String,dangerLevel: String, min: Int, max: Int):MutableList<CountyEnt>

}