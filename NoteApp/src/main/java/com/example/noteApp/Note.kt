package com.example.noteApp

import java.io.Serializable

class Note (val name: String, val dateTime: Long, val content: String, val storage: Int): Serializable