package com.example.mycontact.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.mycontact.Database.Contact
import com.example.mycontact.Database.ContactDB
import com.example.mycontact.Database.Group
import kotlinx.coroutines.*

class ContactViewModel(application: Application): AndroidViewModel(application) {

    private val db = ContactDB.getInstance(getApplication())

    fun getAllContacts(): LiveData<List<Contact>> = db.contactDao().getAllContacts()

    fun getContactById(contactId: Int): Contact = db.contactDao().getContactById(contactId)

    fun updateContactById(contact: Contact, groups: List<Group>) = GlobalScope.launch(Dispatchers.IO)  {
        contact.let { db.contactDao().updateContactById(it.name, it.phone, it.email, it.address, it.id) }
    }

    fun addContact(contact: Contact): Int = runBlocking(Dispatchers.Default) {
        val newId = async { db.contactDao().insert(contact) }.await().toInt()
        return@runBlocking newId
    }
}