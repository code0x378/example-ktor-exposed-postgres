package com.code0x378.notesapp.server

import com.code0x378.notesapp.server.note.NoteService
import com.code0x378.notesapp.server.note.note
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.cors.routing.*
import org.jetbrains.exposed.sql.Database

fun main(args: Array<String>) {
    initDB()
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        jackson {
            registerModule(JavaTimeModule())
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        }
    }
    install(Compression)
    install(DefaultHeaders)
    install(CallLogging)
    install(IgnoreTrailingSlash)
    install(CORS) {
        anyHost()
    }
    routing {
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