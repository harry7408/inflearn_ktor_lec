package com.example.service

import com.example.domain.model.CafeUser
import com.example.domain.repository.CafeUserRepository
import com.example.shared.BCryptPasswordEncoder
import com.example.shared.CafeException
import com.example.shared.CafeUserRole
import com.example.shared.ErrorCode

// Login Service 에서 CafeUserRepository 를 직접 가져오지 않고 1 계층 추가하기 위해 만든 Service
class UserService(
    private val cafeUserRepository: CafeUserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
) {
    // 회원 생성을 위해 만든 Method
    fun createCustomer(nickname: String, plainPassword: String): CafeUser {
        val existedUser = cafeUserRepository.findByNickname(nickname)
        if (existedUser != null) {
            throw CafeException(ErrorCode.USER_ALREADY_EXISTS)
        }
        val hashedPassword = passwordEncoder.encode(plainPassword)
        val cafeUser = CafeUser(
            nickname = nickname,
            password = hashedPassword,
            roles = listOf(CafeUserRole.CUSTOMER)
        )
        return cafeUserRepository.create(cafeUser)
    }

    // 로그인 시 정보를 가져오기 위해 만든 Method (Null Safety 를 이 계층에서 처리)
    fun getCafeUser(nickname: String, plainPassword: String): CafeUser {
        val cafeUser = cafeUserRepository.findByNickname(nickname)
            ?: throw CafeException(ErrorCode.USER_NOT_FOUND)

        // 비밀번호 검증부
        if (!passwordEncoder.matches(
                password = plainPassword,
                hashed = cafeUser.password
            )
        ) {
            throw CafeException(ErrorCode.PASSWORD_INCORRECT)
        }
        return cafeUser
    }

    fun getCafeUserById(cafeUserId: Long): CafeUser {
        return cafeUserRepository.read(cafeUserId)
            ?: throw CafeException(ErrorCode.USER_NOT_FOUND)
    }
}