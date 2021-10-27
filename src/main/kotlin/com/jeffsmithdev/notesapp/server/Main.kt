package com.jeffsmithdev.notesapp.server

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.jeffsmithdev.notesapp.server.note.note
import com.jeffsmithdev.notesapp.server.note.NoteService
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.http.ContentType
import io.ktor.jackson.jackson
import io.ktor.response.respondText
import io.ktor.routing.*
import org.jetbrains.exposed.sql.Database

fun Application.mainModule() {
    initDB()
    install(Compression)
    install(DefaultHeaders)
    install(CallLogging)
    install(IgnoreTrailingSlash)
    install(ContentNegotiation) {
        jackson {
            registerModule(JavaTimeModule())
            enable(SerializationFeature.INDENT_OUTPUT)
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
            configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        }
    }
    install(CORS) {
        anyHost()
    }
    install(Routing) {
        note(NoteService())
        get("/") {
            call.respondText("I love notes!", contentType = ContentType.Text.Plain)
        }
    }
}

fun initDB() {
    val config = HikariConfig("/hikari.properties")
    val ds = HikariDataSource(config)
    Database.connect(ds)
    SeedData.load()
}