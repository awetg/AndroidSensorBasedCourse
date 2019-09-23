package com.example.mycontact.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface ContactDao {

    @Query("SELECT * FROM Contact")
    fun getAllContacts(): LiveData<List<Contact>>

    @Query("SELECT * FROM Contact WHERE id = :contactId")
    fun getContactById(contactId: Int): Contact

    @Insert( onConflict = REPLACE)
    fun insert(contact: Contact): Long

    @Query("UPDATE Contact SET name= :name, phone= :phone, email= :email, address= :address WHERE id= :id")
    fun updateContactById(name: String?, phone: String?, email: String?, address: String?, id: Int?)

}