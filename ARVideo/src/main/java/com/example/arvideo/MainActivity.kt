package com.example.arvideo

import android.content.ContentValues
import android.media.CamcorderProfile
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MotionEvent
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var fragment: ArFragment
    private var decoreRenderable: ModelRenderable? = null
    private lateinit var videoRecorder: VideoRecorder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        take_photo_fab.setOnClickListener { toggleRecording() }

        val adapter = DecorRecyclerAdapter()
        adapter.setClickListener { buildModelRender(it.modelResource) }
        decore_recycler_view.adapter = adapter
        decore_recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        fragment = supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as ArFragment
        fragment.setOnTapArPlaneListener { hitResult: HitResult?, plane: Plane?, motionEvent: MotionEvent? ->
            if (decoreRenderable != null) {

                val anchor = hitResult!!.createAnchor()
                val anchorNode = AnchorNode(anchor)
                anchorNode.setParent(fragment.arSceneView.scene)
                val viewNode = TransformableNode(fragment.transformationSystem)
                viewNode.setParent(anchorNode)
                viewNode.renderable = decoreRenderable
                viewNode.select()
            } else {
                Toast.makeText(applicationContext, "Select decoration item.", Toast.LENGTH_SHORT).show()
            }
        }

        videoRecorder = VideoRecorder()
        videoRecorder.setVideoQuality(CamcorderProfile.QUALITY_2160P, resources.configuration.orientation)
        videoRecorder.setSceneView(fragment.arSceneView)


    }

    private fun buildModelRender(resourceId: Uri?) {
        ModelRenderable.builder()
            .setSource(this, resourceId)
            .build()
            .thenAccept{ it -> decoreRenderable = it }
    }



    private fun toggleRecording() {
        val recording = videoRecorder.onToggleRecord()
        if(recording) {
            take_photo_fab.setImageResource(android.R.drawable.ic_media_pause)
            Toast.makeText(applicationContext, "Recording video.", Toast.LENGTH_SHORT).show()
        } else {
            take_photo_fab.setImageResource(android.R.drawable.ic_media_play)
            Toast.makeText(applicationContext, "Video saved.", Toast.LENGTH_SHORT).show()

            val videoPath = videoRecorder.videoPath?.absolutePath
            val values = ContentValues()
            values.put(MediaStore.Video.Media.TITLE, "Sceneform Video")
            values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
            values.put(MediaStore.Video.Media.DATA, videoPath)
            contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
        }
    }

}
