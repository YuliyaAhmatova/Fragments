package com.example.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Date

class MyAdapter(private val notes: MutableList<Note>) :
    RecyclerView.Adapter<MyAdapter.NoteViewHolder>() {

    private var onNoteClickListener: OnNoteClickListener? = null
    private var onCheckedChangeListener: OnCheckedChangeListener? = null

    interface OnCheckedChangeListener {
        fun onCheckedChange(note: Note, isChecked: Boolean)
    }

    interface OnNoteClickListener {
        fun onNoteClick(note: Note, position: Int)
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nppTV: TextView = itemView.findViewById(R.id.nppTV)
        val dataTV: TextView = itemView.findViewById(R.id.dataTV)
        val textNoteTV: TextView = itemView.findViewById(R.id.textNoteTV)
        val checkBoxCB: CheckBox = itemView.findViewById(R.id.checkBoxCB)
        val infoTV: TextView = itemView.findViewById(R.id.infoTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun getItemCount() = notes.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.nppTV.text = (position + 1).toString()
        holder.dataTV.text = note.data
        holder.textNoteTV.text = note.note
        holder.checkBoxCB.isChecked = note.isChecked == 1
        holder.infoTV.text = if (note.isChecked == 1)"Выполнено" else "Не выполнено"

        holder.checkBoxCB.setOnCheckedChangeListener(null)
        holder.checkBoxCB.setOnCheckedChangeListener {_, isChecked ->
            note.isChecked = if (isChecked) 1 else 0
            onCheckedChangeListener?.onCheckedChange(note, isChecked)
            holder.infoTV.text = if (isChecked) "Выполнено" else "Не выполнено"
        }
//        holder.checkBoxCB.setOnCheckedChangeListener { _, isChecked ->
//            onCheckedChangeListener?.onCheckedChange(note, isChecked)
//            holder.infoTV.text = if (isChecked) "Выполнено" else "Не выполнено"
//        }

        holder.itemView.setOnClickListener {
            if (onNoteClickListener != null) {
                onNoteClickListener?.onNoteClick(note, position)
            }
        }
    }


    fun setOnNoteClickListener(onNoteClickListener: OnNoteClickListener) {
        this.onNoteClickListener = onNoteClickListener
    }

    fun setOnCheckedChangeListener(onCheckedChangeListener: OnCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener
    }
}