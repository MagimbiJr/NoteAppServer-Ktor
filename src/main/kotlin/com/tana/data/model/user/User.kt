package com.tana.data.model.user

import io.ktor.server.auth.*

data class User(
    val email: String,
    val hashPassword: String,
    val name: String
): Principal
