package com.example.mycontact.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mycontact.Database.ContactDB
import com.example.mycontact.Database.ContactGroupJoin
import com.example.mycontact.Database.Group
import kotlinx.coroutines.*

class GroupViewModel(application: Application): AndroidViewModel(application) {

    private val db = ContactDB.getInstance(getApplication())

    fun getAllGroups(): List<Group> = runBlocking(Dispatchers.Default) {
        db.groupDao().getAllGrouop()
    }

    fun addGoup(group: Group) = GlobalScope.launch(Dispatchers.IO) {
        async { db.groupDao().insert(group) }.await()
    }

    fun addContactGroupJoin(id: Int, groups: List<Group>) = GlobalScope.launch(Dispatchers.IO) {
        groups.forEach { db.conactGroupJoinDao().insert(ContactGroupJoin(id, it.id)) }
    }

    fun getAllGroupByContactId(id: Int): List<Group> = runBlocking(Dispatchers.Default) {
        var groups = async { db.conactGroupJoinDao().getAllGroupByContactId(id) }.await()
        if (groups.isEmpty()) groups = async { db.groupDao().getDefaultGroups() }.await()
        return@runBlocking groups
    }
}