package com.example.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Date

class FirstFragment : Fragment() {

    private lateinit var inputNoteET: EditText
    private lateinit var addBTN: Button
    private lateinit var recyclerViewRV: RecyclerView

    @SuppressLint("UseRequireInsteadOfGet")
    private lateinit var db: DBHelper
    private var notes: MutableList<Note> = mutableListOf()
    private var listAdapter: MyAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputNoteET = view.findViewById(R.id.inputNoteET)
        addBTN = view.findViewById(R.id.addBTN)
        recyclerViewRV = view.findViewById(R.id.recyclerViewRV)

        db = DBHelper(requireContext())
        recyclerViewRV.layoutManager = LinearLayoutManager(context)
        recyclerViewRV.setHasFixedSize(true)

        viewDataAdapter()

        addBTN.setOnClickListener {
            saveRecord()
        }
        listAdapter?.setOnCheckedChangeListener(object :
            MyAdapter.OnCheckedChangeListener {
            override fun onCheckedChange(note: Note, isChecked: Boolean) {
                if (isChecked) {
                    note.isChecked = 1
                } else {
                    note.isChecked = 0
                }
                db.updateNote(note)
                viewDataAdapter()
            }
        }
        )
    }

    private fun saveRecord() {
        if (inputNoteET.text.trim().isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Заполнены не все поля",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val number = notes.size + 1
        val data = Date().toString()
        val note = inputNoteET.text.toString()
        val check = 0

        val noteCreate = Note(number, data, note, check)

        db.addNote(noteCreate)

        clearEditFields()
        viewDataAdapter()
    }

    private fun clearEditFields() {
        inputNoteET.text.clear()
    }

    @SuppressLint("UseRequireInsteadOfGet", "NotifyDataSetChanged")
    private fun viewDataAdapter() {
        notes.clear()
        notes.addAll(db.readNote())

        listAdapter = MyAdapter(notes)
        recyclerViewRV.adapter = listAdapter

        listAdapter?.notifyDataSetChanged()
    }
}