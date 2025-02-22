package com.example.config

import com.example.shared.CafeException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

// Frontend 측에 401 에러 코드가 정상적으로 전달되지 못한 문제 해결
fun Application.configureErrorHandling() {
    install(StatusPages) {
        // CafeException 이 발생했을 때 처리해주는 코드 작성
        exception<CafeException>() { call, cause ->
            call.respond(cause.errorCode.httpStatusCode, cause.message ?: "Bad Request")
        }

        // 이외의 예외처리
        exception<Throwable>() { call, cause ->
            // Server 에러일 가능성이 일반적이다
            call.respondText(
                status = HttpStatusCode.InternalServerError, text = "500 : $cause"
            )
        }
    }
}