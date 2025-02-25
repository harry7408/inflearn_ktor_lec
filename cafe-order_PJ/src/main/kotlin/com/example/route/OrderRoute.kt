package com.example.route

import com.example.config.AuthenticatedUser
import com.example.config.AuthenticatedUser.Companion.CUSTOMER_REQUIRED
import com.example.config.authenticatedUser
import com.example.service.OrderService
import com.example.shared.dto.OrderDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.orderRoute() {

    // 주문 관련 Domain Logic 처리하기 위한 계층
    val orderService by inject<OrderService>()


    // 주문 생성과 조회는 고객만 가능하도록 인증 작업
    authenticate(CUSTOMER_REQUIRED) {
        post("/orders") {
            val request = call.receive<OrderDto.CreateRequest>()
            val orderCode = orderService.createOrder(request, call.authenticatedUser())
            call.respond(orderCode)
        }

        // 주문 관련 상태 변경 API
        put("/orders/{orderCode}/status") {
            val orderCode = call.parameters["orderCode"]!!
            val status = call.receive<OrderDto.UpdateStatusRequest>().status
            orderService.updateOrderStatus(orderCode, status, call.authenticatedUser())
            call.respond(HttpStatusCode.OK)
        }
    }
    authenticate(AuthenticatedUser.USER_REQUIRED) {
        // Query Parameter 이름을 매칭 시키면 된다
        get("/orders/{orderCode}") {
            val orderCode = call.parameters["orderCode"]!!
            val orders = orderService.getOrder(orderCode, call.authenticatedUser())

            call.respond(orders)
        }
    }

    authenticate(AuthenticatedUser.ADMINISTER_REQUIRED) {
        get("/orders") {
            val orders: List<OrderDto.DisplayResponse> = orderService.getOrders()
            call.respond(orders)
        }
    }
}