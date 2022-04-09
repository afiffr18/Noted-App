package id.afif.binarchallengech4.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    fun getAllUser() : List<User>

    @Query("SELECT * FROM User WHERE username = :usernameCheck AND password = :passwordCheck")
    fun getUser(usernameCheck:String,passwordCheck:String) : List<User>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User) : Long

    @Update
    fun updateUser(user: User) : Int

    @Delete
    fun deleteUser(user: User) : Int


}