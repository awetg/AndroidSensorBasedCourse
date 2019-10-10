package com.example.fexplorer

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_file_list.view.*
import java.io.File

const val FILE_PARAM = "FILE_PARAM"

class FileListFragment : Fragment() {

    private var fileName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fileName = it.getString(FILE_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_file_list, container, false)
        view.file_recyclerView.layoutManager = LinearLayoutManager(activity)
        view.file_recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

        val root = Environment.getExternalStorageDirectory()
        val file = if (fileName != null) File(root, fileName) else root

        val adapter = FileRecyclerAdapter(file.list()?: arrayOf())
        view.file_recyclerView.adapter = adapter

        adapter.setClickListener {
            val clickedFile = File(file, it)
            if (clickedFile.exists()) {
                if (clickedFile.isDirectory) {
                    fragmentManager?.beginTransaction()
                        ?.replace(R.id.frag_container, newInstance(clickedFile.relativeTo(Environment.getExternalStorageDirectory()).path))
                        ?.addToBackStack(null)?.commit()
                } else {
                    val uri = FileProvider.getUriForFile(context!!, BuildConfig.APPLICATION_ID + ".fileprovider", clickedFile)
                    var mime = context?.contentResolver?.getType(uri)
                    val intent = Intent()
                    intent.setDataAndType(uri, mime)
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        .action = Intent.ACTION_VIEW

                    if (intent.resolveActivity(activity?.packageManager!!) != null) {
                        startActivity(intent)
                    } else {
                        Toast.makeText(activity,R.string.app_not_found, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(fileName: String?) =
            FileListFragment().apply {
                arguments = Bundle().apply {
                    putString(FILE_PARAM, fileName)
                }
            }
    }
}
