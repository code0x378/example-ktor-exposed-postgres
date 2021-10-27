package com.jeffsmithdev.notesapp.server.note

import java.time.LocalDateTime
import java.util.*

data class Note(
        val id: UUID = UUID.randomUUID(),
        val title: String = "",
        val ref: String = "",
        val body: String = "",
        val version: Int = 0,
        val createdAt: LocalDateTime = LocalDateTime.now(),
        val createdBy: String = "",
        val updatedAt: LocalDateTime = LocalDateTime.now(),
        val updatedBy:String = ""
)