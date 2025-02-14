package com.example.domain.model

import com.example.domain.BaseModel
import com.example.shared.CafeUserRole
import kotlinx.serialization.Serializable

@Serializable
data class CafeUser(
    override var id: Long? = null,
    val nickname: String,
    val password: String,
    val roles: List<CafeUserRole>,
) : BaseModel
