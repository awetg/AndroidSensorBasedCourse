package com.example.mycontact.View

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycontact.Database.Group

import com.example.mycontact.R
import com.example.mycontact.Utils.GroupItem
import com.example.mycontact.Utils.GroupsRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_group.view.*

const val SELECTED_GROUPS_EXTRA_KEY = "SELECTED_GROUPS"

class GroupFragment : Fragment() {

    private lateinit var seletedGroups: List<Group>
    private lateinit var adapter: GroupsRecyclerAdapter
    private lateinit var callback: onGroupUpdateListner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            seletedGroups = it.getParcelableArrayList<Group>(SELECTED_GROUPS_EXTRA_KEY) as List<Group>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(R.layout.fragment_group, container, false)

        v.create_group_btn.setOnClickListener { showAddGroupDialog(context) }

        v.group_select_done_btn.setOnClickListener {
            val newSelectedGrps = adapter.getSelectedGroups()
            (targetFragment as EditContactFragment).seletedGroupCB(newSelectedGrps)
            fragmentManager?.popBackStackImmediate()
        }

        val allGroups = callback.getAllGroups()
        adapter = GroupsRecyclerAdapter(allGroups.map { GroupItem(it, seletedGroups.contains(it)) })
        adapter.setClickListener {groupItem, pos ->
            groupItem.selected = !groupItem.selected
            adapter.notifyItemChanged(pos)
        }
        v.group_recyclerV.adapter = adapter
        v.group_recyclerV.layoutManager = LinearLayoutManager(context)

        return v
    }


    companion object {
        @JvmStatic
        fun newInstance(seletedGroups: List<Group>) =
            GroupFragment().apply {
                arguments = Bundle().apply {
                    val groups: ArrayList<Group> = ArrayList(seletedGroups)
                    putParcelableArrayList(SELECTED_GROUPS_EXTRA_KEY,groups)
                }
            }
    }

    interface onGroupUpdateListner {
        fun createGroup(group: Group)
        fun getAllGroups(): List<Group>
    }

    fun setonGroupUpdateListner(callback: onGroupUpdateListner) {
        this.callback = callback
    }

    fun showAddGroupDialog(context: Context?) {
        val gEditText: EditText = EditText(context)
        val dialog: AlertDialog = AlertDialog.Builder(context)
            .setTitle("Add Group")
            .setView(gEditText)
            .setPositiveButton("Add") {dialogInterface, which ->
                val gName = gEditText.text.toString()
                callback.createGroup(Group(gName))
                val allGrps = callback.getAllGroups()
                adapter.purgeAdd(allGrps.map { GroupItem(it, seletedGroups.contains(it)) })
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }
}
