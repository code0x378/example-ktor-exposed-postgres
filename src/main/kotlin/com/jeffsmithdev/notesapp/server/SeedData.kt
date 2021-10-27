package com.jeffsmithdev.notesapp.server

import com.jeffsmithdev.notesapp.server.note.Notes
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

object SeedData {

    fun load() {
        transaction {
            if (Notes.selectAll().count() == 0) {
                Notes.deleteWhere { Notes.title like "%%" }
                for (i in 1..1000)
                    Notes.insert {
                        it[id] = UUID.randomUUID()
                        it[ref] = "test${i}"
                        it[title] = "test${i}"
                        it[body] = "I am testing note #${i}"
                        it[version] = 0
                        it[createdAt] = DateTime()
                        it[createdBy] = "taco"
                        it[updatedAt] = DateTime()
                        it[updatedBy] = "taco"
                    }
            }
        }
    }
}
