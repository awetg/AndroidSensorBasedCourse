package com.example.catFacts

import android.os.Handler
import android.util.Log
import org.json.JSONObject
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class Connection(mHand: Handler) : Runnable {
    val myHandler = mHand
    override fun run() {
        try {
            val myUrl = URL("https://cat-fact.herokuapp.com/facts/random")

            val myConn = myUrl.openConnection() as HttpURLConnection
            myConn.requestMethod = "GET"

//            myConn.doOutput = true
//            val outputStream = myConn.getOutputStream()
//            outputStream.bufferedWriter().write("FirstName=${fnmae}&LastName=${lname}")

            val inputStream: InputStream = myConn.getInputStream()
            val allText = inputStream.bufferedReader().readText()

            val result = StringBuilder()
            result.append(allText)
            val json = result.toString()
            val fact = JSONObject(json).optString("text")
            val msg = myHandler.obtainMessage()
            msg.what = 0
            msg.obj = fact
            myHandler.sendMessage(msg)

        } catch (e: Exception) {
            e.stackTrace
            Log.e("DBG", e.message)
        }
    }
}