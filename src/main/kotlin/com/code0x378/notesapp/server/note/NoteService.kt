package com.code0x378.notesapp.server.note

import com.code0x378.notesapp.server.ServiceHelper.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import java.util.*

object Notes : Table() {
    val id = uuid("id")
    val title = text("title")
    val body = text("body")
    val ref = text("ref")
    val version = integer("version")
    val createdAt = datetime("created_at")
    val createdBy = text("created_by")
    val updatedAt = datetime("updated_at")
    val updatedBy = text("updated_by")
}

class NoteService {

    @kotlinx.coroutines.ObsoleteCoroutinesApi
    suspend fun getAllNotes(): List<Note> = dbQuery {
        Notes.selectAll().map { toNote(it) }
    }

    @kotlinx.coroutines.ObsoleteCoroutinesApi
    suspend fun getNote(id: UUID): Note? = dbQuery {
        Notes.select {
            (Notes.id eq id)
        }.mapNotNull { toNote(it) }
            .singleOrNull()
    }

    @kotlinx.coroutines.ObsoleteCoroutinesApi
    suspend fun addNote(note: Note): Note {
        dbQuery {
            Notes.insert {
                it[id] = note.id
                it[ref] = note.ref
                it[title] = note.title
                it[body] = note.body
                it[version] = note.version + 1
                it[createdAt] = LocalDateTime.now()
                it[createdBy] = note.createdBy
                it[updatedAt] = LocalDateTime.now()
                it[updatedBy] = note.updatedBy
            }
        }
        return getNote(note.id)!!
    }

    @kotlinx.coroutines.ObsoleteCoroutinesApi
    suspend fun updateNote(note: Note): Note {
        val id = note.id
        return run {
            dbQuery {
                Notes.update({ Notes.id eq id }) {
                    it[Notes.id] = note.id
                    it[ref] = note.ref
                    it[title] = note.title
                    it[body] = note.body
                    it[version] = note.version + 1
                    it[createdAt] = note.createdAt
                    it[createdBy] = note.createdBy
                    it[updatedAt] = LocalDateTime.now()
                    it[updatedBy] = note.updatedBy
                }
            }
            getNote(id)!!
        }
    }

    @kotlinx.coroutines.ObsoleteCoroutinesApi
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
