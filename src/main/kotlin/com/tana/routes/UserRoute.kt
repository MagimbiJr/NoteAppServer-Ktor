package com.tana.routes

import com.tana.authentication.JwtService
import com.tana.data.model.user.LoginUser
import com.tana.data.model.user.RegisterUser
import com.tana.data.model.Response
import com.tana.data.model.user.User
import com.tana.repository.NoteServerDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes(
    db: NoteServerDao,
    jwtService: JwtService,
    hashFunction: (String) -> String
) {
    route("/user") {
        post("/register") {
            val registerUser = try {
                call.receive<RegisterUser>()
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.Conflict,
                    Response(false, "One or all fields are missing")
                )
                return@post
            }
            try {
                val user = User(registerUser.email, hashFunction(registerUser.password), registerUser.name)
                db.addUser(user = user)
                call.respond(
                    HttpStatusCode.OK,
                    Response(true, jwtService.generateToken(user))
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.Conflict,
                    Response(false, e.localizedMessage ?: "An unknown error occurred")
                )
            }
        }
        post("/login") {
            val loginUser = try {
                call.receive<LoginUser>()
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.Conflict,
                    Response(false, "One or all fields are missing")
                )
                return@post
            }
            try {
                val user = db.findUserByEmail(loginUser.email)
                if (user == null) {
                    call.respond(
                        HttpStatusCode.NotFound,
                        Response(false, "No user with such email")
                    )
                } else {
                    if (hashFunction(loginUser.password) == user.hashPassword) {
                        call.respond(
                            HttpStatusCode.OK,
                            Response(true, jwtService.generateToken(user))
                        )
                    } else {
                        call.respond(
                            HttpStatusCode.Conflict,
                            Response(false, "Invalid password")
                        )
                    }
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.Conflict,
                    Response(false, e.localizedMessage ?: "An unknown error occurred")
                )
            }
        }
    }
}
