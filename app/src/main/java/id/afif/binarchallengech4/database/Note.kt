package id.afif.binarchallengech4.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id:Int?,
    @ColumnInfo(name = "user_id") val userId :Int,
    @ColumnInfo(name = "judul") val judul:String,
    @ColumnInfo(name = "catatan") val catatan:String
)
