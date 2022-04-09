package id.afif.binarchallengech4.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note WHERE user_id = :userId")
    fun getAllNote(userId : Int) : List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note) : Long

    @Update
    fun updateNote(note: Note) : Int

    @Delete
    fun deleteNote(note: Note) : Int

}