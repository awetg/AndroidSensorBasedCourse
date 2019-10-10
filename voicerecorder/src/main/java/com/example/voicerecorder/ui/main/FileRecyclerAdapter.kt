package com.example.voicerecorder.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.voicerecorder.R
import kotlinx.android.synthetic.main.file_list_item.view.*
import java.io.File

class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class FileRecyclerAdapter(val fileList: Array<String>) : RecyclerView.Adapter<FileViewHolder>() {

    private var clickListner: (String) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        return FileViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.file_list_item,parent,false) as LinearLayout
        )
    }

    override fun getItemCount(): Int = fileList.size

    fun setClickListener(newClickListener: (String) -> Unit) {
        clickListner = newClickListener
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.itemView.file_name_txt.text = fileList[position]
        holder.itemView.setOnClickListener{ clickListner(fileList[position]) }
    }
}