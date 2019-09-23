package com.example.noteApp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.note_list_item.view.*

class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class NoteRecyclerAdapter(var noteList: List<Note>) : RecyclerView.Adapter<NoteViewHolder>() {

    private var clickListner: (Note) -> Unit = { _ -> }
    private var longClickListner: (Note) -> Boolean = { _ -> true}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.note_list_item, parent, false
            ) as ConstraintLayout
        )
    }

    fun setClickListener(newClickListener: (Note) -> Unit) {
        clickListner = newClickListener
    }

    fun setOnLongClickListener(nLongClickListner: (Note) -> Boolean) {
        longClickListner = nLongClickListner
    }

    fun purgeAdd(newList: List<Note>) {
        noteList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount() = noteList.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.itemView.note_title_item.text = noteList[position].name
        holder.itemView.note_content_item.text = noteList[position].content
        holder.itemView.setOnClickListener{ clickListner(noteList[position]) }
        holder.itemView.setOnLongClickListener { longClickListner(noteList[position]) }
    }
}