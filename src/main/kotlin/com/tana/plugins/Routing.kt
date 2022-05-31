package com.tana.plugins

import com.tana.authentication.JwtService
import com.tana.repository.NoteServerDao
import com.tana.routes.noteRoutes
import com.tana.routes.userRoutes
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting(
    db: NoteServerDao,
    jwtService: JwtService,
    hashFunction: (String) -> String
) {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        userRoutes(db, jwtService, hashFunction)
        noteRoutes(db, hashFunction)
    }
}
