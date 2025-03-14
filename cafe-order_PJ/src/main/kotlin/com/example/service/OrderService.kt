package com.example.service

import com.example.config.AuthenticatedUser
import com.example.domain.model.CafeOrder
import com.example.domain.repository.CafeOrderRepository
import com.example.shared.CafeException
import com.example.shared.CafeOrderStatus
import com.example.shared.CafeUserRole
import com.example.shared.ErrorCode
import com.example.shared.dto.OrderDto
import java.time.LocalDateTime
import java.util.*

class OrderService(
    private val menuService: MenuService,
    private val userService: UserService,
    private val cafeOrderRepository: CafeOrderRepository,
) {
    // 주문 생성 API
    fun createOrder(
        request: OrderDto.CreateRequest, authenticatedUser: AuthenticatedUser
    ): String {
        val menu = menuService.getMenu(request.menuId)
        val order = CafeOrder(
            orderCode = "OC${UUID.randomUUID()}",
            cafeMenuId = menu.id!!,
            cafeUserId = authenticatedUser.userId,
            price = menu.price,
            status = CafeOrderStatus.READY,
            orderedAt = LocalDateTime.now()
        )

        return cafeOrderRepository.create(order).orderCode
    }


    fun getOrder(
        orderCode: String, authenticatedUser: AuthenticatedUser
    ): OrderDto.DisplayResponse {
        // order Code로 주문조회 -> 주문자와 요청자 검증 -> 주문 데이터 클래스변환, 응답
        val order = getOrderByCode(orderCode)
        checkOrderOwner(authenticatedUser, order)

        return OrderDto.DisplayResponse(
            orderCode = order.orderCode,
            menuName = menuService.getMenu(order.cafeMenuId).name,
            customerName = userService.getCafeUserById(order.cafeUserId).nickname,
            price = order.price,
            status = order.status,
            orderedAt = LocalDateTime.now(),
            id = null,
        )
    }


    fun updateOrderStatus(
        orderCode: String,
        status: CafeOrderStatus,
        authenticatedUser: AuthenticatedUser
    ) {
        // 주문 조회
        val order: CafeOrder = getOrderByCode(orderCode)
        // 주문자와 요청자가 동일한지 비교
        checkOrderOwner(authenticatedUser, order)

        // 고객은 취소만 가능하도록 (But 관리자는 고객 + 관리자 2가지 역할을 가지고 있다)
        checkCustomerAction(authenticatedUser, status)

        order.update(status)
        cafeOrderRepository.update(order)
    }

    private fun getOrderByCode(orderCode: String): CafeOrder {
        val order: CafeOrder = cafeOrderRepository.findByCode(orderCode)
            ?: throw CafeException(ErrorCode.ORDER_NOT_FOUND)
        return order
    }

    // Customer 인 경우에만 만족하도록 수정
    private fun checkOrderOwner(
        authenticatedUser: AuthenticatedUser,
        order: CafeOrder,
    ) {
        if (authenticatedUser.isOnlyCustomer()) {
            if (authenticatedUser.userId != order.cafeUserId) {
                throw CafeException(ErrorCode.FORBIDDEN)
            }
        }
    }

    private fun checkCustomerAction(
        authenticatedUser: AuthenticatedUser,
        status: CafeOrderStatus
    ) {
        if (authenticatedUser.userRoles == listOf(CafeUserRole.CUSTOMER)) {
            if (status != CafeOrderStatus.CANCEL) {
                throw CafeException(ErrorCode.FORBIDDEN)
            }
        }
    }

    fun getOrders(): List<OrderDto.DisplayResponse> {
        return cafeOrderRepository.findAllOrders()
    }

    fun getOrderStats(): List<OrderDto.StatsResponse> {
        // DB QUERY 사용 방식
        return cafeOrderRepository.findOrderStats()


       /* // 서버에서 연산하는 방법
        val orders: List<CafeOrder> = cafeOrderRepository.findAll()

        return orders.groupBy { it.orderedAt.toLocalDate() }
            .map { (date, list) ->
                OrderDto.StatsResponse(
                    orderDate =date,
                    totalOrderCount = list.count().toLong(),
                    totalOrderPrice = list.sumOf { it.price }.toLong()
                )
            }.sortedByDescending { it.orderDate }*/
    }
}


