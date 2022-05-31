package com.tana.routes

import com.tana.authentication.JwtService
import com.tana.data.model.Response
import com.tana.data.model.note.Note
import com.tana.data.model.user.User
import com.tana.repository.NoteServerDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.noteRoutes(
    db: NoteServerDao,
    hashFunction: (String) -> String
) {
    authenticate("jwt-auth") {
        route("/notes") {
            get {
                try {
                    val email = call.principal<User>()!!.email
                    val notes = db.getNotes(email)
                    call.respond(
                        HttpStatusCode.OK,
                        notes
                    )
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        emptyList<Note>()
                    )
                }
            }
            post("/create") {
                val note = try {
                    call.receive<Note>()
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        Response(false, "Missing values")
                    )
                    return@post
                }
                try {
                    val email = call.principal<User>()!!.email
                    db.addNote(note, email)
                    call.respond(
                        HttpStatusCode.OK,
                        Response(true, "Note added successfully")
                    )
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        Response(false, e.localizedMessage ?: "An unknown error occurred")
                    )
                }
            }
            post("/edit") {
                val note = try {
                    call.receive<Note>()
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        Response(false, "Missing values")
                    )
                    return@post
                }
                try {
                    val email = call.principal<User>()!!.email
                    db.editNote(note, email)
                    call.respond(
                        HttpStatusCode.OK,
                        Response(true, "Note updated successfully")
                    )
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        Response(false, e.localizedMessage ?: "An unknown error occurred")
                    )
                }
            }
            delete("/delete") {
                val noteId = try {
                    call.request.queryParameters["id"]!!
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        Response(false, "No note with such id")
                    )
                    return@delete
                }
                try {
                    val email = call.principal<User>()!!.email
                    db.deleteNote(noteId, email)
                    call.respond(
                        HttpStatusCode.OK,
                        Response(true, "Note deleted successfully")
                    )
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        Response(false, e.localizedMessage ?: "An unknown error occurred")
                    )
                }
            }
        }
    }
}