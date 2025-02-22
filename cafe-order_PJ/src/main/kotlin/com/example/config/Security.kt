package com.example.config

import com.example.config.AuthenticatedUser.Companion.CUSTOMER_REQUIRED
import com.example.shared.CafeUserRole
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*


fun Application.configureSecurity() {
    install(Authentication) {
        session<AuthenticatedUser>(CUSTOMER_REQUIRED) {
            // Principal? Type 반환-> Marker Interface
            validate { session ->
                session.takeIf { it.userRoles.contains(CafeUserRole.CUSTOMER) }
            }

            // 인증이 통과되지 않은 경우
            challenge {
                call.respond(HttpStatusCode.Forbidden,"only for customer")
            }
        }
    }
}
// call에 대한 확장함수
fun ApplicationCall.authenticatedUser() : AuthenticatedUser = authentication.principal<AuthenticatedUser>()!!