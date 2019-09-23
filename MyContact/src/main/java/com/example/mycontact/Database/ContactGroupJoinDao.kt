package com.example.mycontact.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.RoomWarnings

@Dao
interface ContactGroupJoinDao {

    @Query("SELECT groupId FROM contact_group_join WHERE contactId = :contactId")
    fun getGoupIdByContactId(contactId: Int): List<Int>

    @Query("SELECT contactId FROM contact_group_join WHERE groupId = :groupId")
    fun getContactIdByGroupId(groupId: Int): List<Int>

    @Insert( onConflict = REPLACE)
    fun insert(contactGroupJoin: ContactGroupJoin): Long

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM `group` JOIN (SELECT groupId FROM contact_group_join WHERE contactId = :id) t1 ON `Group`.id = t1.groupId")
    fun getAllGroupByContactId(id: Int): List<Group>
}