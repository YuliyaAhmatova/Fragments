package com.example.fragments

import java.io.Serializable

class Note(
    var number: Int,
    val data: String,
    val note: String,
    var isChecked: Int
) : Serializable