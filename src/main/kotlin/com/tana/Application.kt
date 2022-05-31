package com.tana

import com.tana.authentication.JwtService
import com.tana.authentication.hash
import io.ktor.server.application.*
import com.tana.plugins.*
import com.tana.repository.DatabaseFactory
import com.tana.repository.NoteServerDao

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val db = NoteServerDao()
    val jwtService = JwtService()
    val hashFunction = { s: String -> hash(s) }

    DatabaseFactory.init()
    configureRouting(db, jwtService, hashFunction)
    configureSerialization()
    configureSecurity(  db, jwtService)
}
