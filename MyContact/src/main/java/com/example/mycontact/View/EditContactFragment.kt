package com.example.mycontact.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mycontact.Database.Contact
import com.example.mycontact.Database.Group

import com.example.mycontact.R
import kotlinx.android.synthetic.main.fragment_edit_contact.*
import kotlinx.android.synthetic.main.fragment_edit_contact.view.*

const val SELECTED_GROUP_REQUEST_CODE = 2323

class EditContactFragment : Fragment() {

    private var contact: Contact? = null
    private lateinit var groups: List<Group>
    private var addingContact = false
    private lateinit var callback: OnUpdateContactListner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contact = it.getParcelable(SELECTED_CONTACT_EXTRA_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addingContact = contact == null
        groups = callback.getGroupForContact(contact?.id ?: -1)
        val v = inflater.inflate(R.layout.fragment_edit_contact, container, false)
        if (!addingContact) {
            v.name_eTxt_edit_contact.setText(contact?.name)
            v.phone_eTxt_edit_contact.setText(contact?.phone)
            v.email_eText_edit_contact.setText(contact?.email)
            v.address_eTxt_edit_contact.setText(contact?.address)
        }
        v.group_eText_edit_contact.setText(groups.map { it.name }.joinToString(", "))

        v.cancel_btn_edit_contact.setOnClickListener { fragmentManager?.beginTransaction()?.remove(this)?.commit() }

        v.save_btn_edit_contact.setOnClickListener { saveContact() }

        v.group_eText_edit_contact.setOnClickListener { launchGroupFragment() }

        return v
    }

    interface OnUpdateContactListner {
        fun onUpdateContact(contact: Contact, groups: List<Group> ,create: Boolean)
        fun getGroupForContact(id: Int): List<Group>
    }

    fun setOnUPdateContactListner(callback: OnUpdateContactListner) {
        this.callback = callback
    }

    fun saveContact() {
        val c = Contact(name_eTxt_edit_contact.text.toString(),
            phone_eTxt_edit_contact.text.toString(),
            email_eText_edit_contact.text.toString(),
            address_eTxt_edit_contact.text.toString(),
            contact?.id ?: 0)
        callback.onUpdateContact(c, groups, addingContact)
        fragmentManager?.beginTransaction()?.remove(this)?.commit()
    }

    fun launchGroupFragment() {
        val groupFragment = GroupFragment.newInstance(groups)
        groupFragment.setTargetFragment(this, SELECTED_GROUP_REQUEST_CODE)
        fragmentManager?.beginTransaction()?.add(R.id.home_frag_container, groupFragment)
            ?.addToBackStack(null)?.commit()
    }

    fun seletedGroupCB(seletedGroup: List<Group>) {
        groups = seletedGroup
        group_eText_edit_contact.setText(groups.map { it.name }.joinToString(", "))
    }

    companion object {
        @JvmStatic
        fun newInstance(contact: Contact?) =
            EditContactFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(SELECTED_CONTACT_EXTRA_KEY, contact)
                }
            }
    }
}
