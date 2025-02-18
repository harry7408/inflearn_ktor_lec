package com.example.service

import com.example.config.AuthenticatedUser
import com.example.config.AuthenticatedUser.Companion.SESSION_NAME
import com.example.shared.dto.UserDto
import io.ktor.server.sessions.*

class LoginService(
    private val userService: UserService
) {
    fun login(cafeUserLoginRequest: UserDto.LoginRequest, currentSession: CurrentSession) {
        checkNoSession(currentSession)

        // cafeUser 조회 후 유저가 없으면 에러 있으면 비밀번호 검증 -> 세션 저장 및 응답
        val user = userService.getCafeUser(
            cafeUserLoginRequest.nickname,
            cafeUserLoginRequest.plainPassword
        )

        currentSession.set(
            AuthenticatedUser(user.id!!, user.roles)
        )
    }

    fun signup(cafeUserSignupRequest: UserDto.LoginRequest, currentSession: CurrentSession) {
        checkNoSession(currentSession)

        val user = userService.createCustomer(
            cafeUserSignupRequest.nickname,
            cafeUserSignupRequest.plainPassword
        )

        currentSession.set(
            AuthenticatedUser(user.id!!, user.roles)
        )
    }

    fun logout(currentSession: CurrentSession) {
//        currentSession.clear<AuthenticatedUser>() 로도 해제 가능
//         세션 만료와 Key 값을 동시에 날린다
        currentSession.clear(name = SESSION_NAME)
    }

    private fun checkNoSession(currentSession: CurrentSession) {
        val authenticatedUser = currentSession.get<AuthenticatedUser>()
        if (authenticatedUser != null) {
            throw RuntimeException()
        }
    }
}