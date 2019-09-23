package com.example.w4_d4_5_arlab

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.decore_item.view.*


class DecorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class DecorRecyclerAdapter : RecyclerView.Adapter<DecorViewHolder>() {

    private var clickListner: (DecorItem) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DecorViewHolder {
        return DecorViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.decore_item,parent,false) as ConstraintLayout
        )
    }

    override fun getItemCount(): Int  = DecoreItemList.allItems.size

    fun setClickListener(newClickListener: (DecorItem) -> Unit) {
        clickListner = newClickListener
    }

    override fun onBindViewHolder(holder: DecorViewHolder, position: Int) {
        val imgRes = DecoreItemList.allItems[position].imgResource
        if (imgRes != 0) holder.itemView.decore_img.setImageResource(imgRes)
        holder.itemView.decore_name.text = DecoreItemList.allItems[position].name
        holder.itemView.setOnClickListener{ clickListner(DecoreItemList.allItems[position]) }
    }

}