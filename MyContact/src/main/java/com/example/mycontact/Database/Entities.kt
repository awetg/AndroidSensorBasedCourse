package com.example.mycontact.Database

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Contact(
    val name: String?,
    val phone: String?,
    val email: String?,
    val address: String?,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
): Parcelable


@Parcelize
@Entity
data class Group(
    val name: String?,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
): Parcelable

@Entity(tableName = "contact_group_join",
    primaryKeys = arrayOf("contactId","groupId"),
    foreignKeys = arrayOf(
        ForeignKey(entity = Contact::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("contactId")),
        ForeignKey(entity = Group::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("groupId"))
    )
)
data class ContactGroupJoin(
    val contactId: Int,
    val groupId: Int
)
