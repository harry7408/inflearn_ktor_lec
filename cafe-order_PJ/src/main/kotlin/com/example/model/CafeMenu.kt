package com.example.domain.model

import com.example.shared.CafeMenuCategory
import kotlinx.serialization.Serializable

// Json 데이터로 직렬화 가능해진다
@Serializable
data class CafeMenu(
    val name: String,
    val price: Int,
    val category: CafeMenuCategory,
    val image: String,
    var id: Long? = null,
)
