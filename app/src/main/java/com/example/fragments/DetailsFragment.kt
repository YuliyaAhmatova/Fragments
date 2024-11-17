package com.example.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class DetailsFragment : Fragment(), OnFragmentDataListener {
    private lateinit var onFragmentDataListener: OnFragmentDataListener

    private lateinit var updateNoteET: EditText
    private lateinit var updateBTN: Button
    private var note: Note? = null
    private var notes: MutableList<Note> = mutableListOf()
    private var item: Int = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onFragmentDataListener = requireActivity() as OnFragmentDataListener
        val view = inflater.inflate(R.layout.fragment_details, container, false)
        updateNoteET = view.findViewById(R.id.updateNoteET)
        updateBTN = view.findViewById(R.id.updateBTN)
        var note: Note = arguments?.getSerializable("note") as Note
        item = arguments?.getInt("item") as Int
        updateNoteET.setText(note.note)
        updateBTN.setOnClickListener {
            val value = updateNoteET.text.toString()
            val updatedNote = Note(note.number, note.data, value, note.isChecked)
            onData(item, updatedNote)
        }
        return view
    }

    override fun onData(item: Int, note: Note) {
        val bundle = Bundle()
        bundle.putSerializable("newNote", note)
        bundle.putInt("newItem", item)

        val transaction = this.fragmentManager?.beginTransaction()
        val firstFragment = FirstFragment()
        firstFragment.arguments = bundle

        transaction?.replace(R.id.main, firstFragment)
        transaction?.addToBackStack(null)
        transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction?.commit()
    }
}