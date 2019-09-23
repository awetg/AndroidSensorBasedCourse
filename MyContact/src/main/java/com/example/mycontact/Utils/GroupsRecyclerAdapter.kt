package com.example.mycontact.Utils


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mycontact.Database.Group
import com.example.mycontact.R
import kotlinx.android.synthetic.main.group_list_item.view.*

class GroupItem(val group: Group,var selected: Boolean)

class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class GroupsRecyclerAdapter(private var groupList: List<GroupItem> = listOf()) : RecyclerView.Adapter<GroupViewHolder>() {

    private var clickListner: (GroupItem, Int) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        return GroupViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.group_list_item,parent,false) as LinearLayout
        )
    }

    override fun getItemCount(): Int = groupList.size

    fun setClickListener(newClickListener: (GroupItem, Int) -> Unit) {
        clickListner = newClickListener
    }

    fun purgeAdd(groupItems: List<GroupItem>) {
        groupList = groupItems
        notifyDataSetChanged()
    }

    fun getSelectedGroups():List<Group> = groupList.filter { it.selected }.map { it.group }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val item = groupList[position]
        holder.itemView.group_checkbox.isChecked = item.selected
        holder.itemView.group_item_name.text = item.group.name
        holder.itemView.setOnClickListener{ clickListner(groupList[position], position) }
    }
}