package pt.ulusofona.deisi.a2020.cm.g15.data.room.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
data class TestEnt (
    var result: Boolean = false,
    var location : String,
    var date: String,
    var iconImageResource: Int,
    var testImage: Int
):Parcelable {

    @PrimaryKey
    var uuid: String = UUID.randomUUID().toString()
}