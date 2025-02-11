package com.example

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        // End-Point + repondText로 Response 를 준다
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
