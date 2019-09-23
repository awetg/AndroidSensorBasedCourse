package com.example.fexplorer

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.FragmentTransaction
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
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_file_list, container, false)
        val root = Environment.getExternalStorageDirectory()
        val file = if (fileName != null) File(root, fileName) else root
        val adapter = FileRecyclerAdapter(file.list()?: arrayOf())
        adapter.setClickListener {
            val file = File(root, it)
            if (file.isDirectory) {
                val fTransaction: FragmentTransaction? = fragmentManager?.beginTransaction()
                fTransaction?.replace(R.id.home_frag_container, FileListFragment.newInstance(it!!))
                    ?.addToBackStack(null)?.commit()
            } else {
                val uri = FileProvider.getUriForFile(context!!, BuildConfig.APPLICATION_ID + ".provider", file)
                val mime = context?.contentResolver?.getType(uri)
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.setDataAndType(uri, mime)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(intent)
            }
        }
        v.file_recyclerView.adapter = adapter
        v.file_recyclerView.layoutManager = LinearLayoutManager(activity)
        v.file_recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        return v
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
