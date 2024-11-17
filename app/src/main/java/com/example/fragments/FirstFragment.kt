package com.example.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Date

class FirstFragment : Fragment() {
    private lateinit var onFragmentDataListener: OnFragmentDataListener

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
        onFragmentDataListener = requireActivity() as OnFragmentDataListener
        val view = inflater.inflate(R.layout.fragment_first, container, false)
        inputNoteET = view.findViewById(R.id.inputNoteET)
        addBTN = view.findViewById(R.id.addBTN)
        recyclerViewRV = view.findViewById(R.id.recyclerViewRV)

        db = DBHelper(requireContext())
        recyclerViewRV.layoutManager = LinearLayoutManager(context)
        recyclerViewRV.setHasFixedSize(true)
        return view
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        viewDataAdapter()

        val newNote = arguments?.getSerializable("newNote") as Note?
        val newItem = arguments?.getInt("newItem")
        if (newNote != null) {
            notes.clear()
            notes.addAll(db.readNote())
            if (newItem != null) {
                swap(newItem, newNote, notes)
            }
            listAdapter = MyAdapter(notes)
            recyclerViewRV.adapter = listAdapter
            listAdapter?.notifyDataSetChanged()
        }

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
        listAdapter?.setOnNoteClickListener(object :
            MyAdapter.OnNoteClickListener {
            override fun onNoteClick(note: Note, position: Int) {
                val builder = AlertDialog.Builder(requireContext())
                builder
                    .setTitle("Внимание!")
                    .setMessage("Предполагаемые действия")
                    .setPositiveButton("Изменить") { _, _ ->
                        notes = db.readNote()
                        onFragmentDataListener.onData(position, note)
                    }
                    .setNegativeButton("Удалить") { _, _ ->
                        db.deleteNote(note)
                        viewDataAdapter()
                    }
                    .create().show()
            }
        })
    }

    private fun swap(newItem: Int, newNote: Note, notes: MutableList<Note>) {
        notes.add(newItem + 1, newNote)
        notes.removeAt(newItem)
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