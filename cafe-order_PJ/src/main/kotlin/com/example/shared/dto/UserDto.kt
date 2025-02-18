package com.example.shared.dto

import kotlinx.serialization.Serializable

class UserDto {

    @Serializable
    data class LoginRequest(
        val nickname: String,
        // 암호화 하지 않은 Password
        val plainPassword : String
    )
}