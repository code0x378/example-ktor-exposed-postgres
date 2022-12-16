package com.code0x378.notesapp.server

import com.code0x378.notesapp.server.note.Notes
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.*

object SeedData {

    fun load() {
        transaction {
            if (Notes.selectAll().count() == 0L) {
                Notes.deleteWhere { Notes.title like "%%" }
                for (i in 1..1000)
                    Notes.insert {
                        it[id] = UUID.randomUUID()
                        it[ref] = "test${i}"
                        it[title] = "test${i}"
                        it[body] = "I am testing note #${i}"
                        it[version] = 0
                        it[createdAt] = LocalDateTime.now()
                        it[createdBy] = "taco"
                        it[updatedAt] = LocalDateTime.now()
                        it[updatedBy] = "taco"
                    }
            }
        }
    }
}
