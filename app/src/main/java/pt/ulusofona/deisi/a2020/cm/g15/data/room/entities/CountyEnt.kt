package pt.ulusofona.deisi.a2020.cm.g15.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CountyEnt(
    @PrimaryKey
    var county: String,
    var district: String,
    var date: String,
    var incidence: Int,
    var riskLevel: String,
    var cases14days: Int,
    var population: Int
    ){}