package com.example.sensorApp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.sensor_list_item.view.*


class SensorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class SensorRecyclerAdapter : RecyclerView.Adapter<SensorViewHolder>() {

    private var clickListner: (SensorItem) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorViewHolder {
        return SensorViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.sensor_list_item, parent, false
            ) as ConstraintLayout
        )
    }

    fun setClickListener(newClickListener: (SensorItem) -> Unit) {
        clickListner = newClickListener
    }

    override fun getItemCount() = sensorItemList.size

    override fun onBindViewHolder(holder: SensorViewHolder, position: Int) {
        holder.itemView.sensor_icon.setImageResource(sensorItemList[position].imgResource)
        holder.itemView.sensor_name_txt.text = sensorItemList[position].name
        holder.itemView.setOnClickListener{ clickListner(sensorItemList[position]) }
    }
}