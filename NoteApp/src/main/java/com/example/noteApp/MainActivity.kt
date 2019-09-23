package com.example.noteApp

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.lang.StringBuilder

const val ROOT_APP_FOLDER = "NoteApp"
const val PRIVATE_STORAGE = 0
const val PUBLIC_STORAGE = 1

class MainActivity : AppCompatActivity() {

    private val gson = GsonBuilder().create()
    private lateinit var adapter: NoteRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        add_note_fab.setOnClickListener {
            CurrenteNote.value = null
            startActivity(Intent(this, EditNoteActivity::class.java))
        }

        adapter = NoteRecyclerAdapter(loadFiles())
        adapter.setClickListener { note ->
            CurrenteNote.value = note
            startActivity(Intent(this, EditNoteActivity::class.java))
        }
        adapter.setOnLongClickListener {note ->
            val dialog = AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes",DialogInterface.OnClickListener { _, _ ->
                    deleteNote(note)
                })
                .setNegativeButton("No", null)
                .setCancelable(false)
            dialog.show()
            true
        }
        note_list_recycler.adapter = adapter
        note_list_recycler.layoutManager = LinearLayoutManager(this)
        note_list_recycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        checkPermission()

        loadFiles().forEach { Log.d("DBG", it.content) }
    }

    private fun checkPermission() {
        if ((Build.VERSION.SDK_INT >= 23 &&
                    checkSelfPermission( android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        ) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
        }
    }

    private fun loadFiles(): List<Note> {
        val privateDir = this.getExternalFilesDir(null)
        val root = Environment.getExternalStorageDirectory()
        val publicDir = File(root, "/$ROOT_APP_FOLDER")

        return getNotesForDir(privateDir) + getNotesForDir(publicDir)
    }

    override fun onResume() {
        super.onResume()
        adapter.purgeAdd(loadFiles())
    }

    private fun getNotesForDir(dir: File?): ArrayList<Note> {
        val noteList: ArrayList<Note> = arrayListOf()
        val noteFiles: ArrayList<String> = arrayListOf()

        dir?.list()?.forEach {
            if (it.endsWith(".txt")) noteFiles.add(it)
        }

//        var fileInputStream: InputStream?
//        var objectInputStream: ObjectInputStream?

        var inputStream: InputStream?
        var inputStreamReader: InputStreamReader?
        var bufferedReader: BufferedReader?
        noteFiles.forEach { noteFile ->
            try {
//                fileInputStream = FileInputStream(File(privateDir, noteFile))
//                objectInputStream = ObjectInputStream(fileInputStream)
//                val note = objectInputStream?.readObject() as? Note
//                note?.let { noteList.add(it) }
//                objectInputStream?.close()
//                fileInputStream?.close()
                inputStream = FileInputStream(File(dir, noteFile))
                inputStreamReader = InputStreamReader(inputStream)
                bufferedReader = BufferedReader(inputStreamReader)
                val stringBuilder = StringBuilder()
                var line: String? = bufferedReader?.readLine()
                while (line != null) {
                    stringBuilder.append(line)
                    line = bufferedReader?.readLine()
                }
                val json = stringBuilder.toString()
                gson.fromJson(json, Note::class.java)?.let {
                        note -> noteList.add(note)
                }
                bufferedReader?.close()
                inputStreamReader?.close()
                inputStream?.close()
            }catch (e: IOException) {
                e.printStackTrace()
                Log.e("DBG", "Error getting note", e)
            }
        }
        return noteList
    }

    private fun deleteNote(note: Note) {
        val dir = if(note.storage == PRIVATE_STORAGE)
            this.getExternalFilesDir(null)
        else
            File(Environment.getExternalStorageDirectory(),"/$ROOT_APP_FOLDER")
        val file = File(dir, note.name + ".txt")
        if (file.exists()) {
            val deleted = file.delete()
            Toast.makeText(this, "File ${if(!deleted) "not deleted" else "deleted"}", Toast.LENGTH_LONG).show()
            if(deleted) {
                adapter.purgeAdd(loadFiles())
            }

        } else {
            Toast.makeText(this, "File not found", Toast.LENGTH_LONG).show()
        }
    }

}
