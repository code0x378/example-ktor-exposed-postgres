package com.fattysmurff.notesapp.server.note

import org.joda.time.DateTime
import java.util.*

data class Note(
        val id: UUID = UUID.randomUUID(),
        val title: String = "",
        val ref: String = "",
        val body: String = "",
        val version: Int = 0,
        val createdAt: DateTime = DateTime(),
        val createdBy: String = "",
        val updatedAt: DateTime = DateTime(),
        val updatedBy:String = ""
)