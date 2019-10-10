package com.example.voicerecorder

import android.os.Bundle
import android.os.Environment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.example.voicerecorder.ui.main.SectionsPagerAdapter
import com.example.w3_d1_lab.MyAudioRecorder
import java.io.File
import java.lang.Long

internal var recording = false
internal var playing = false

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { view ->
            if (recording) {
                recording = false
                fab.setBackgroundResource(android.R.drawable.ic_media_play)
            } else {
                recording = true
                playing = false
                fab.setBackgroundResource(R.drawable.ic_stop_white_24dp)
                val dir = Environment.getExternalStorageDirectory()
                val file = File(dir, "VoiceRecorder/sample" + Long.toHexString(System.currentTimeMillis()) + ".raw")
                Thread(MyAudioRecorder(file)).start()
            }
        }
    }
}