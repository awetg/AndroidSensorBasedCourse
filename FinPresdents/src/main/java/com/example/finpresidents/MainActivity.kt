package com.example.retrofit_adapter

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_president.view.*
import okhttp3.ResponseBody

import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response


class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // create the adapter to convert the array to views
        val adapter = PresidentListAdapter(this, GlobalModel.presidents)

        // attach the adapter to a ListView
        mainlistview.adapter = adapter

        mainlistview.setOnItemClickListener { _, _, position, _ ->
            val selectedPresident = GlobalModel.presidents[position]
            selname.text = "Name: ${selectedPresident.name}"
            seldescription.text = selectedPresident.description

            callWebService(selectedPresident.name)
        }

        mainlistview.setOnItemLongClickListener { _, _, position, _ ->
            val selectedPresident = GlobalModel.presidents[position]
            val detailIntent = PresidentDetailActivity.newIntent(this, selectedPresident)
            startActivity(detailIntent)
            true
        }
    }

    private inner class PresidentListAdapter(context: Context, private val presidents: MutableList<President>) : BaseAdapter() {
        private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getCount(): Int = presidents.size

        override fun getItem(position: Int) = presidents[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val rowView = inflater.inflate(R.layout.item_president, parent, false)

            val thisPresident = presidents[position]
            rowView.tvName.text = thisPresident.name
            rowView.tvStartDuty.text = Integer.toString(thisPresident.startDuty)
            rowView.tvEndDuty.text = Integer.toString(thisPresident.endDuty)

            return rowView
        }

    }


    fun callWebService(selectedPresidentName: String) {
        val callHits = DemoApi.service.presidentName("query", "json", "search", selectedPresidentName)
        val callImg = DemoApi.service.presidenImage("query", "json", "pageimages", 300, selectedPresidentName)
        val preseidntHits = object : Callback<DemoApi.Model.Results> {
            override fun onResponse(call: Call<DemoApi.Model.Results>, response:Response<DemoApi.Model.Results>?) {
                if (response != null) {
                    val body = response.body()!!
                     totalHits.text = "Hits: ${body.query.searchInfo?.totalhits.toString()}"
                }
            }

            override fun onFailure(call: Call<DemoApi.Model.Results>, t: Throwable) {
                Log.e("DBG", t.toString());
            }
        }

        val presidentImg = object : Callback<DemoApi.Model.Results> {
            override fun onResponse(call: Call<DemoApi.Model.Results>, response:Response<DemoApi.Model.Results>?) {
                if (response != null) {
                    Log.d("DBG", Gson().toJson(response))
                    val body = response.body()!!
                    val imgSource = body.query.pages?.values?.toList()?.first()?.thumbnail?.source
                    if (imgSource != null) {
                        val call = DemoApi.service.loadImage(imgSource)
                        val loadImg = object : Callback<ResponseBody> {
                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Log.d("DBG", t.message)
                            }

                            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                if (response.isSuccessful) {
                                    val bitmap = BitmapFactory.decodeStream(response.body()?.byteStream())
                                    pres_img.setImageBitmap(bitmap)
                                }
                            }

                        }
                        call.enqueue(loadImg)
                    } else {
                        pres_img.setImageResource(R.drawable.img_holder)
                    }
                }
            }

            override fun onFailure(call: Call<DemoApi.Model.Results>, t: Throwable) {
                Log.e("DBG", "Failure", t);
            }
        }


        callHits.enqueue(preseidntHits) // asynchronous request
        callImg.enqueue(presidentImg)
    }
}
