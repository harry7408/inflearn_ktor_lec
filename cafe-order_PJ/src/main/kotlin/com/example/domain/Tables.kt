package com.example.domain

import com.example.shared.CafeMenuCategory
import com.example.shared.CafeOrderStatus
import com.example.shared.CafeUserRole
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

// menu
// LongIdTable : Primary KEY로 Long Type을 사용
// enumerationByName 을 사용하지 않으면 Enum Type을 숫자로 저장
object CafeMenuTable : LongIdTable(name = "cafe_menu") {
    val name = varchar("menu_name", length = 50)
    val price = integer("price")
    val category = enumerationByName("category", 10, CafeMenuCategory::class)
    val image = text("image")
}

//user (admin, user)
object CafeUserTable : LongIdTable(name = "cafe_user") {
    val nickname = varchar("nickname", length = 50)
    val password = varchar("password", length = 100)

    // 관리자는 고객도 될 수 있게 설계하여 Custom Table이 들어갔다 (Column 안에 값이 2개 들어갈 수 있도록)
    val roles = enumList("roles", CafeUserRole::class.java, varcharLength = 20)
}

// order
// 파란색 Italic 글씨체 : Extension Function 을 의미
object CafeOrderTable : LongIdTable(name = "cafe_order") {
    val orderCode = varchar("order_code", length = 50)
    val cafeUserId = reference("cafe_user_id", CafeUserTable.id)
    val cafeMenuId = reference("cafe_menu_id", CafeMenuTable.id)
    val price = integer("price")
    val status = enumerationByName("status", length = 10, CafeOrderStatus::class)
    val orderedAt = datetime("ordered_at")
}

