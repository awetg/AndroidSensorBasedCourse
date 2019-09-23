package com.example.mycontact.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycontact.Database.Contact
import com.example.mycontact.Database.Group
import com.example.mycontact.R
import com.example.mycontact.Utils.ContactsRecyclerAdapter
import com.example.mycontact.ViewModel.ContactViewModel
import com.example.mycontact.ViewModel.GroupViewModel
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity(), EditContactFragment.OnUpdateContactListner, GroupFragment.onGroupUpdateListner {

    private lateinit var contactViewModel: ContactViewModel
    private lateinit var groupViewModel: GroupViewModel
    private lateinit var adapter: ContactsRecyclerAdapter

    private lateinit var fTransaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//        contact_recyclerV.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))

        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
        groupViewModel = ViewModelProviders.of(this).get(GroupViewModel::class.java)

        adapter = ContactsRecyclerAdapter()
        adapter.setClickListener { contact ->  addFragment(ContactDetailFragment.newInstance(contact)) }
        contactViewModel.getAllContacts().observe(this, Observer { contacts -> adapter.purgeAdd(contacts) })
        contact_recyclerV.adapter = adapter
        contact_recyclerV.layoutManager = LinearLayoutManager(this)

        create_contact_txtV.setOnClickListener { addFragment(EditContactFragment.newInstance(null)) }
    }

    fun addFragment(fragment: Fragment) {
        fTransaction = supportFragmentManager.beginTransaction()
        fTransaction.replace(R.id.home_frag_container, fragment)
        fTransaction.addToBackStack(null);
        fTransaction.commit()
    }

    override fun onUpdateContact(contact: Contact, groups: List<Group>, create: Boolean) {
        if (create) {
            val id = contactViewModel.addContact(contact)
            groupViewModel.addContactGroupJoin(id, groups)
        } else {
            contactViewModel.updateContactById(contact,groups)
            groupViewModel.addContactGroupJoin(contact.id, groups)
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is EditContactFragment) {

            fragment.setOnUPdateContactListner(this)

        } else if (fragment is GroupFragment) {

            fragment.setonGroupUpdateListner(this)
        }
    }

    override fun getGroupForContact(id: Int): List<Group> = groupViewModel.getAllGroupByContactId(id)

    override fun createGroup(group: Group) {
        groupViewModel.addGoup(group)
    }

    override fun getAllGroups(): List<Group> = groupViewModel.getAllGroups()
}
