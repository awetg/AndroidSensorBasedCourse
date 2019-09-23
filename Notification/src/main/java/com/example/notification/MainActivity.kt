package com.example.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

const val CHANNEL_ID = "myUniqueID"

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private var counter = 1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView.text = counter.toString()

        // create notification channel if SDK is >= oreo
        createNotificationChannel()

        setSupportActionBar(toolbar)

        increment_btn.setOnClickListener { view ->
            counter++
            textView.text = counter.toString()
            Snackbar.make(view, "Number incremented", Snackbar.LENGTH_LONG)
                .setAction("UNDO") {
                    counter--
                    textView.text = counter.toString()
                }
                .show()
        }


        toast_btn.setOnClickListener {
            Toast.makeText(this, R.string.toast_msg, Toast.LENGTH_SHORT).show()
        }

        notification_btn.setOnClickListener { showNotification() }
    }

    private fun createNotificationChannel() {

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             val channel = NotificationChannel(CHANNEL_ID,getString(R.string.channel_name),NotificationManager.IMPORTANCE_HIGH)
             channel.description = getString(R.string.channel_description)
             channel.enableLights(true)
             channel.enableVibration(true)
             // Register the channel with the system
             val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
             notificationManager.createNotificationChannel(channel)
         }
    }

    private fun showNotification() {
        // create notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText("Counter value is at $counter")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setColor(if(Build.VERSION.SDK_INT<=23) resources.getColor(R.color.colorAccent) else getColor(R.color.colorPrimary))
            .build()
        NotificationManagerCompat.from(this).notify(0, notification)
    }
}
