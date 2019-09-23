package com.example.mycontact.Utils

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mycontact.Database.Contact
import com.example.mycontact.R
import kotlinx.android.synthetic.main.cotact_list_item.view.*
import kotlin.random.Random


class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class ContactsRecyclerAdapter(private var contactList: MutableList<Contact> = mutableListOf()) : RecyclerView.Adapter<ContactViewHolder>() {

    private var clickListner: (Contact) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.cotact_list_item,parent,false) as LinearLayout
        )
    }

    override fun getItemCount(): Int = contactList.size

    fun setClickListener(newClickListener: (Contact) -> Unit) {
        clickListner = newClickListener
    }

    fun purgeAdd(contacts: List<Contact>) {
        contactList = contacts.toMutableList()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.itemView.contact_name.text = contactList[position].name
        holder.itemView.contact_image.text = contactList[position].name?.get(0).toString()
        val color = Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        holder.itemView.contact_image.backgroundTintList = ColorStateList.valueOf(color)
        holder.itemView.setOnClickListener{ clickListner(contactList[position]) }
    }
}