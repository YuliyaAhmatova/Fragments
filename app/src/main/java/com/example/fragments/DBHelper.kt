package com.example.fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_NAME = "NOTE_DATABASE"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "note_table"
        val KEY_NUMBER = "number"
        val KEY_DATA = "data"
        val KEY_NOTE = "note"
        val KEY_ISCHECKED = "isChecked"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " (" +
                KEY_NUMBER + " INTEGER PRIMARY KEY, " +
                KEY_DATA + " TEXT, " +
                KEY_NOTE + " TEXT, " +
                KEY_ISCHECKED + " INTEGER" + ")")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addNote(note: Note) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NUMBER, note.number)
        values.put(KEY_DATA, note.data)
        values.put(KEY_NOTE, note.note)
        values.put(KEY_ISCHECKED, note.isChecked)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    @SuppressLint("Range", "Recycle")
    fun readNote(): MutableList<Note> {
        val noteList: MutableList<Note> = mutableListOf()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return noteList
        }
        var noteNumber: Int
        var noteData: String
        var noteNote: String
        var noteCheck: Int
        if (cursor.moveToFirst()) {
            do {
                noteNumber = cursor.getInt(cursor.getColumnIndex("number"))
                noteData = cursor.getString(cursor.getColumnIndex("data"))
                noteNote = cursor.getString(cursor.getColumnIndex("note"))
                noteCheck = cursor.getInt(cursor.getColumnIndex("isChecked"))
                val note = Note(noteNumber, noteData, noteNote, noteCheck)
                noteList.add(note)
            } while (cursor.moveToNext())
        }
        cursor?.close()
        db?.close()
        return noteList
    }

    fun updateNote(note: Note) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NUMBER, note.number)
        contentValues.put(KEY_DATA, note.data)
        contentValues.put(KEY_NOTE, note.note)
        contentValues.put(KEY_ISCHECKED, note.isChecked)
        db.update(TABLE_NAME, contentValues, "data=?", arrayOf(note.data))
        db.close()
    }

    fun deleteNote(note: Note) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_DATA, note.data)
        db.delete(TABLE_NAME, "data=?", arrayOf(note.data))
        db.close()
    }
}