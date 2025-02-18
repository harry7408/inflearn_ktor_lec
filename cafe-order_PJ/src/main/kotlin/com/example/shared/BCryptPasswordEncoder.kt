package com.example.shared

import org.mindrot.jbcrypt.BCrypt

// 비밀번호 Hashing 시 많이 사용되는 방식
class BCryptPasswordEncoder {

    fun encode(password : String) : String {
        return BCrypt.hashpw(password,BCrypt.gensalt())
    }

    fun matches(password: String, hashed: String) : Boolean {
        return BCrypt.checkpw(password,hashed)
    }
}