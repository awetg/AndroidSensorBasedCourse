package com.example.mycontact.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface GroupDao {

    @Query("SELECT * FROM `Group`")
    fun getAllGrouop(): List<Group>

    @Insert( onConflict = REPLACE)
    fun insert (group: Group): Long

    @Query("SELECT * FROM `Group` WHERE name LIKE :name")
    fun getDefaultGroups(name: String = "My contact"): List<Group>
}