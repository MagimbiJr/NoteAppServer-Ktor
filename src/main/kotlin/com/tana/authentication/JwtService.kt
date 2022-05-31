package com.tana.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.tana.data.model.user.User

class JwtService {

    val realm = "ktor sample app"
    private val domain = "https://jwt-provider-domain/"
    private val audience = "jwt-audience"
    private val issuer = "Note Server Application"
    private val secret = System.getenv("JWT_SECRET")
    private val algorithm = Algorithm.HMAC256(secret)

    val verifier = JWT.require(Algorithm.HMAC256(secret))
        .withAudience(audience)
        .withIssuer(issuer)
        .build()

    fun generateToken(user: User): String = JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("email", user.email)
        .sign(algorithm)
}