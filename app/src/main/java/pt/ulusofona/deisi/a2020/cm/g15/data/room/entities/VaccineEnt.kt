package pt.ulusofona.deisi.a2020.cm.g15.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VaccineEnt(
    @PrimaryKey
    var id: Int = 0,
    var data:Long,
    val inoculacao1Ac: Int,
    val inoculacao2Ac: Int,
    val vacinadosAc: Int
) {
}