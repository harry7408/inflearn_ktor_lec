package com.example.config

import com.example.config.AuthenticatedUser.Companion.SESSION_NAME
import com.example.shared.CafeUserRole
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable

fun Application.configureSession() {
    // Plugin 을 install 후 람다 함수에 세부 설정
    install(Sessions) {
        // AuthenticatedUser Type을 메모리에 저장
        cookie<AuthenticatedUser>(SESSION_NAME, SessionStorageMemory()) {
            cookie.path = "/"
        }
    }
}


@Serializable
data class AuthenticatedUser(
    val userId: Long,
    val userRoles: List<CafeUserRole>
) : Principal  /* 마커 interface 상속*/ {

    fun isOnlyCustomer(): Boolean {
        return userRoles == listOf(CafeUserRole.CUSTOMER)
    }

    companion object {
        // 데이터가 없음을 표현 (Null Safety 를 위함)
        fun none() = AuthenticatedUser(0, listOf())
        const val SESSION_NAME = "CU_SESSION_ID"
        const val USER_REQUIRED = "user-required"
        const val CUSTOMER_REQUIRED = "customer-required"
        const val ADMINISTER_REQUIRED = "administer-required"
    }
}