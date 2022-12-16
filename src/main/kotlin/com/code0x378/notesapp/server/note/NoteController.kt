package com.code0x378.notesapp.server.note

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.note(noteService: NoteService) {

    route("/notes") {

        get("") {
            call.respond(noteService.getAllNotes())
        }

        get("/{id}") {
            val note = noteService.getNote(UUID.fromString(call.parameters["id"]))
            if (note == null) call.respond(HttpStatusCode.NotFound)
            else call.respond(note)
        }

        post("") {
            val note = call.receive<Note>()
            call.respond(noteService.addNote(note))
        }

        put("") {
            val note = call.receive<Note>()
            call.respond(noteService.updateNote(note))
        }

        delete("/{id}") {
            val removed = noteService.deleteNote(UUID.fromString(call.parameters["id"]))
            if (removed) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound)
        }
    }
}
