package com.example.w3_d1_lab

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import com.example.voicerecorder.recording
import java.io.*

class MyAudioRecorder(val recordFile: File) : Runnable {

    override fun run() {

        try {
            val outputStream = FileOutputStream(recordFile)
            val bufferedOutputStream = BufferedOutputStream(outputStream)
            val dataOutputStream = DataOutputStream(bufferedOutputStream)


            val minBufferSize = AudioRecord.getMinBufferSize(44100,AudioFormat.CHANNEL_OUT_STEREO,AudioFormat.ENCODING_PCM_16BIT)
            val aFormat = AudioFormat.Builder()
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setSampleRate(44100)
                .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                .build()
            val recorder = AudioRecord.Builder()
                .setAudioSource(MediaRecorder.AudioSource.MIC)
                .setAudioFormat(aFormat)
                .setBufferSizeInBytes(minBufferSize)
                .build()
            val audioData = ByteArray(minBufferSize)
            recorder.startRecording()

            while (recording) {
                val numofBytes = recorder.read(audioData, 0, minBufferSize)
                if(numofBytes>0) {
                    Log.d("DBG", numofBytes.toString())
                    dataOutputStream.write(audioData)
                }
            }
            recorder.stop()
            dataOutputStream.close()
        }
        catch (e: IOException) {
        Log.d("DBG", e.message)
        }
    }

}