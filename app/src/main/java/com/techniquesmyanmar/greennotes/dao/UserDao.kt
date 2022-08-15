package com.techniquesmyanmar.greennotes.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.techniquesmyanmar.greennotes.entity.Notes
import com.techniquesmyanmar.greennotes.entity.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAllUsers() : LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM user WHERE id =:id")
    suspend fun deleteSpecificUser(id:Int)

    @Query("SELECT * FROM user WHERE id =:id")
    fun getUserById(id:Int) : LiveData<User>
}