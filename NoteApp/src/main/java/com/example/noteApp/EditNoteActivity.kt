package com.example.noteApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_edit_note.*
import com.google.gson.GsonBuilder
import java.io.*

class EditNoteActivity : AppCompatActivity() {

    val gson = GsonBuilder().create()
    var selectedStorage: Int = PRIVATE_STORAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        if (CurrenteNote.value != null) {
            CurrenteNote.value?.let {
                note_title_txt.setText(it.name)
                note_content_txt.setText(it.content)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_save_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_private_menu -> saveExternalPrivate()
            R.id.save_public_menu -> saveExternalPublic()
            R.id.close_note_menu -> finish()
        }
        return true
    }

    private fun saveExternalPrivate() {
        selectedStorage = PRIVATE_STORAGE
        val note = getCurrentNote()
        val folder = this.getExternalFilesDir(null)
        val file = File(folder, "/${note.name}.txt")
        if(!file.exists()) file.parentFile.mkdirs()
        saveFile(file)
    }

    private fun saveExternalPublic() {
        selectedStorage = PUBLIC_STORAGE
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val note = getCurrentNote()
            val rootFolder = Environment.getExternalStorageDirectory()
            val file = File(rootFolder, "/$ROOT_APP_FOLDER/${note.name}.txt")
            if(!file.exists()) file.parentFile.mkdirs()
//            file.writeText("text")
                saveFile(file)
        } else {
            Toast.makeText(this, "External storage not ready", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveFile(file: File) {
        try {
//            val fileOutputStream = FileOutputStream(file)
//            val objectOutputStream = ObjectOutputStream(fileOutputStream)
//            objectOutputStream.writeObject(currentNote)
//            objectOutputStream.close()
//            fileOutputStream.close()

            val outputStream = FileOutputStream(file)
            val json: String = gson.toJson(getCurrentNote())
            outputStream.write(json.toByteArray())
            outputStream.close()
            Toast.makeText(this, "Note saved", Toast.LENGTH_LONG).show()
            finish()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("DBG", "Error saving note", e)
            Toast.makeText(this, "Error saving note!!", Toast.LENGTH_LONG).show()
        }
    }

    private fun getCurrentNote(): Note {
        val name = note_title_txt.text.toString()
        val content = note_content_txt.text.toString()
        return Note(name, System.currentTimeMillis(), content, selectedStorage)
    }
}
