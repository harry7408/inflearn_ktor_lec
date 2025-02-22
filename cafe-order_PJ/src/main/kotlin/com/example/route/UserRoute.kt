package com.example.route

import com.example.config.AuthenticatedUser
import com.example.service.LoginService
import com.example.shared.dto.UserDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject

fun Route.userRoute() {

    val loginService by inject<LoginService>()

    // 인증된 사용자의 정보 표시 (세션에서 정보를 가져오면 된다)
    get("/me") {
        val user = call.sessions.get<AuthenticatedUser>() ?: AuthenticatedUser.none()
        call.respond(user)
    }

    // 닉네임, Password 를 받아 검증
    post("/login") {
        val user = call.receive<UserDto.LoginRequest>()
        loginService.login(user, call.sessions)
        call.respond(HttpStatusCode.OK)
    }

    // 닉네임, Password 를 받아 회원여부 검증
    post("/signup") {
        val user = call.receive<UserDto.LoginRequest>()
        loginService.signup(user, call.sessions)
        call.respond(HttpStatusCode.OK)
    }

    // 세션과 쿠키 만료하기
    post("/logout") {
        loginService.logout(call.sessions)
        call.respond(HttpStatusCode.OK)
    }
}