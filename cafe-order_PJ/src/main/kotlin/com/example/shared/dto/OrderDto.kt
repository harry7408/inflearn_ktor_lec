package com.example.shared.dto

import com.example.shared.CafeOrderStatus
import com.example.shared.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

class OrderDto {
    @Serializable
    data class CreateRequest(val menuId: Long)

    @Serializable
    data class DisplayResponse(
        val orderCode: String,
        val menuName: String,
        val customerName: String,
        val price: Int,
        var status: CafeOrderStatus,
        // Java 의 Class
        @Serializable(with = LocalDateTimeSerializer::class)
        val orderedAt: LocalDateTime,
        var id: Long? = null,
    )

    @Serializable
    // 상태 변경 요청의 RequestBody
    data class UpdateStatusRequest(
        val status: CafeOrderStatus
    )
}