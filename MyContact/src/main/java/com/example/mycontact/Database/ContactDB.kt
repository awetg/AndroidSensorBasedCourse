package com.example.mycontact.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Contact::class, Group::class, ContactGroupJoin::class], version = 1, exportSchema = false)
abstract class ContactDB : RoomDatabase() {
    abstract fun contactDao(): ContactDao
    abstract fun groupDao(): GroupDao
    abstract fun conactGroupJoinDao(): ContactGroupJoinDao




    companion object {

        private val roomDBCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.execSQL(
                    "INSERT INTO 'Group' ( name ) VALUES ( 'My contact'), ( 'Friends' ), ( 'Family' ), ( 'Coworkers' )"
                )
            }
        }

        private var instance: ContactDB? = null

        @Synchronized
        fun getInstance(context: Context): ContactDB {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(context.applicationContext, ContactDB::class.java, "contact.db")
                        .addCallback(roomDBCallback)
                        .build()
            }
            return instance!!
        }
    }

}