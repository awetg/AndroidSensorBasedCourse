package com.example.dogImages

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadDogImage()
        reload_btn.setOnClickListener { loadDogImage() }
    }

    private fun isNetworkAvailable():Boolean? {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo?.isConnected
    }

    private fun loadDogImage() {
        if (isNetworkAvailable() == true) {
            val myURLparams = URLparams(URL("https://dog.ceo/api/breeds/image/random"))
            GetConn().execute(myURLparams)
        } else {
            Toast.makeText(this, "No connection, The app needs connection to work", Toast.LENGTH_LONG).show()
        }
    }

    data class URLparams(val url: URL)

    inner class GetConn : AsyncTask<URLparams, Unit, Bitmap>() {
        override fun doInBackground(vararg urlp: URLparams): Bitmap {
            lateinit  var bitmap:Bitmap
            try {
                val myConn = urlp[0].url.openConnection() as HttpURLConnection
                val inputStream: InputStream = myConn.getInputStream()

                // get json data and extract img url
                val result = StringBuilder()
                result.append(inputStream.bufferedReader().readText())
                val json = result.toString()
                val imgURl = JSONObject(json).optString("message")

                // load image
                bitmap = BitmapFactory.decodeStream((URL(imgURl).openConnection() as HttpURLConnection).inputStream)

            } catch (e: Exception) {
                e.stackTrace
                Log.e("DBG", "Connection error ", e);
            }
            return bitmap

        }

        override fun onPostExecute(result: Bitmap?) {
            dog_img.setImageBitmap(result)
        }
    }
}
