package com.example.mycontact.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.mycontact.Database.Contact

import com.example.mycontact.R
import kotlinx.android.synthetic.main.fragment_contact_detail.view.*

const val SELECTED_CONTACT_EXTRA_KEY = "SELECTED_CONTACT"

class ContactDetailFragment : Fragment() {

    private var contact: Contact? = null

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
        val v = inflater.inflate(R.layout.fragment_contact_detail, container, false)
        v.detail_frag_contact_name.text = contact?.name
        v.detail_frag_phone.text = contact?.phone ?: "No phone number"
        v.detail_frag_email.text = contact?.email ?: "No emial"
        v.detail_frag_address.text = contact?.address ?: "No Address"

        v.edit_contact_btn.setOnClickListener{
            val fTransaction: FragmentTransaction? = fragmentManager?.beginTransaction()
            fTransaction?.replace(R.id.home_frag_container, EditContactFragment.newInstance(contact!!))
                ?.addToBackStack(null)?.commit()
        }

        return v
    }

    companion object {
        @JvmStatic
        fun newInstance(contact: Contact) =
            ContactDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(SELECTED_CONTACT_EXTRA_KEY, contact)
                }
            }
    }
}
