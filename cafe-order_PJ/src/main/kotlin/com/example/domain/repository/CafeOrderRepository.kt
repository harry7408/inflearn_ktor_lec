package com.example.domain.repository

import com.example.domain.CafeMenuTable
import com.example.domain.CafeOrderTable
import com.example.domain.CafeUserTable
import com.example.domain.ExposedCrudRepository
import com.example.domain.model.CafeOrder
import com.example.shared.dto.OrderDto
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.JavaLocalDateColumnType
import org.jetbrains.exposed.sql.javatime.JavaLocalDateTimeColumnType
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.time.LocalDate

class CafeOrderRepository(
    override val table: CafeOrderTable
) : ExposedCrudRepository<CafeOrderTable, CafeOrder> {
    override fun toRow(domain: CafeOrder): CafeOrderTable.(InsertStatement<EntityID<Long>>) -> Unit = {
        if (domain.id != null) {
            it[id] = domain.id!!
        }
        it[orderCode] = domain.orderCode
        it[cafeUserId] = domain.cafeUserId
        it[cafeMenuId] = domain.cafeMenuId
        it[price] = domain.price
        it[status] = domain.status
        it[orderedAt] = domain.orderedAt
    }

    override fun toDomain(row: ResultRow): CafeOrder {
        return CafeOrder(
            orderCode = row[CafeOrderTable.orderCode],
            cafeMenuId = row[CafeOrderTable.cafeMenuId].value,
            cafeUserId = row[CafeOrderTable.cafeUserId].value,
            price = row[CafeOrderTable.price],
            status = row[CafeOrderTable.status],
            orderedAt = row[CafeOrderTable.orderedAt],
        ).apply {
            id = row[CafeOrderTable.id].value
        }
    }

    override fun updateRow(domain: CafeOrder): CafeOrderTable.(UpdateStatement) -> Unit = {
        it[orderCode] = domain.orderCode
        it[cafeUserId] = domain.cafeUserId
        it[cafeMenuId] = domain.cafeMenuId
        it[price] = domain.price
        it[status] = domain.status
        it[orderedAt] = domain.orderedAt
    }

    fun findByCode(orderCode: String): CafeOrder? = dbQuery {
        table.selectAll()
            .where { table.orderCode.eq(orderCode) }
            .map(::toDomain)
            .firstOrNull()
    }

    /**
     * select o.order_code,
     *        o.price,
     *        o.status,
     *        o.ordered_at,
     *        m.menu_name,
     *        u.nickname
     * from cafe_order o
     *          inner join cafe_user u on u.id = o.cafe_user_id
     *          inner join cafe_menu m on m.id = o.cafe_menu_id
     * order by o.id desc;
     */

    fun findAllOrders(): List<OrderDto.DisplayResponse> = dbQuery {
        val query = table
            .innerJoin(CafeUserTable)
            .innerJoin(CafeMenuTable)
            .select(
                CafeOrderTable.orderCode,
                CafeOrderTable.price,
                CafeOrderTable.status,
                CafeOrderTable.orderedAt,
                CafeOrderTable.id,
                CafeMenuTable.name,
                CafeUserTable.nickname,
            ).orderBy(CafeOrderTable.id to SortOrder.DESC)

        query.map {
            OrderDto.DisplayResponse(
                orderCode = it[table.orderCode],
                menuName = it[CafeMenuTable.name],
                customerName = it[CafeUserTable.nickname],
                price = it[table.price],
                status = it[table.status],
                orderedAt = it[table.orderedAt],
                id = it[table.id].value
            )
        }
    }

    /**
     * query 방식은 DB 연산 부하를 준다 (DATETIME 을 DATE 로 바꾸는 과정)
     * SELECT cast(ORDERED_AT AS DATE) order_date, count(*), sum(price)
     * FROM CAFE_ORDER
     * GROUP BY order_date
     * ORDER BY order_date DESC;
     */
    fun findOrderStats(): List<OrderDto.StatsResponse> = dbQuery {
        // QUERY
        val countExpression = table.id.count().alias("count")
        val priceExpression = table.price.sum().alias("price")
        val orderDateExpression = table.orderedAt.castTo<LocalDate>(JavaLocalDateColumnType())

        table.select(
            orderDateExpression,
            countExpression,
            priceExpression
        ).groupBy(orderDateExpression)
            .orderBy(orderDateExpression to SortOrder.DESC)
            .map {
                OrderDto.StatsResponse(
                    orderDate = it[orderDateExpression],
                    totalOrderCount = it[countExpression],
                    totalOrderPrice = it[priceExpression]?.toLong() ?: 0
                )
            }
    }
}