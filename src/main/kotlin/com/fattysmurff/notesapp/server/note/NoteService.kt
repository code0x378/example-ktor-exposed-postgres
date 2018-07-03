package com.fattysmurff.notesapp.server.note

import com.fattysmurff.notesapp.server.ServiceHelper.dbQuery
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime
import java.util.*

object Notes : Table() {
    val id = uuid("id").primaryKey()
    val title = text("title")
    val body = text("body")
    val ref = text("ref")
    val version = integer("version")
    val createdAt = date("created_at")
    val createdBy = text("created_by")
    val updatedAt = date("updated_at")
    val updatedBy = text("updated_by")
}

class NoteService {

    suspend fun getAllNotes(): List<Note> = dbQuery {
        Notes.selectAll().map { toNote(it) }
    }

    suspend fun getNote(id: UUID): Note? = dbQuery {
        Notes.select {
            (Notes.id eq id)
        }.mapNotNull { toNote(it) }
                .singleOrNull()
    }

    suspend fun addNote(note: Note): Note {
        dbQuery {
            Notes.insert() {
                it[id] = note.id
                it[ref] = note.ref
                it[title] = note.title
                it[body] = note.body
                it[version] = note.version + 1
                it[createdAt] = DateTime()
                it[createdBy] = note.createdBy
                it[updatedAt] = DateTime()
                it[updatedBy] = note.updatedBy
            }
        }
        return getNote(note.id)!!
    }

    suspend fun updateNote(note: Note): Note {
        val id = note.id
        return if (id == null) {
            addNote(note)
        } else {
            dbQuery {
                Notes.update({ Notes.id eq id }) {
                    it[Notes.id] = note.id
                    it[ref] = note.ref
                    it[title] = note.title
                    it[body] = note.body
                    it[version] = note.version + 1
                    it[createdAt] = note.createdAt
                    it[createdBy] = note.createdBy
                    it[updatedAt] = DateTime()
                    it[updatedBy] = note.updatedBy
                }
            }
            getNote(id)!!
        }
    }

    suspend fun deleteNote(id: UUID): Boolean = dbQuery {
        Notes.deleteWhere { Notes.id eq id } > 0
    }

    private fun toNote(row: ResultRow): Note =
            Note(
                    id = row[Notes.id],
                    ref = row[Notes.ref],
                    title = row[Notes.title],
                    body = row[Notes.body],
                    version = row[Notes.version],
                    createdAt = row[Notes.createdAt],
                    createdBy = row[Notes.createdBy],
                    updatedAt = row[Notes.updatedAt],
                    updatedBy = row[Notes.updatedBy]
            )
}
