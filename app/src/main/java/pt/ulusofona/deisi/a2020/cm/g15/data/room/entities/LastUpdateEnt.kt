package pt.ulusofona.deisi.a2020.cm.g15.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LastUpdateEnt(
    @PrimaryKey
    var id: Int = 0,
    var data: String,
    var confirmadosNovos: Int,
    val recuperados : Int,
    val obitos : Int,
    val internados : Int,
    val internadosUci : Int
    ) {}