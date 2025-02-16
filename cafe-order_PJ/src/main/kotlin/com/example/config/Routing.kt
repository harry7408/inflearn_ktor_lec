package com.example.config

import com.example.service.MenuService
import com.example.shared.CafeOrderStatus
import com.example.shared.dto.OrderDto
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.time.LocalDateTime


fun Application.configureRouting() {

    // Koin 을 활용하여 의존성 주입
    val cafeMenuService by inject<MenuService>()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("/api") {
            get("/menus") {
                val list = cafeMenuService.findAll()
                // 응답할 때 사용
                call.respond(list)
            }

            post("/orders") {
                val request = call.receive<OrderDto.CreateRequest>()
                val selectedMenu = cafeMenuService.getMenu(request.menuId)
                val order = OrderDto.DisplayResponse(
                    orderCode = "diam",
                    menuName = selectedMenu.name,
                    customerName = "Pedro Velez",
                    price = selectedMenu.price,
                    status = CafeOrderStatus.READY,
                    orderedAt = LocalDateTime.now(),
                    id = 1
                )
                call.respond(order)
            }

            // Query Parameter 이름을 매칭 시키면 된다
            get("/orders/{orderCode}") {
                val orderCode = call.parameters["orderCode"]!!

                val order = OrderDto.DisplayResponse(
                    orderCode = orderCode,
                    menuName = "아메리카노",
                    customerName = "Pedro Velez",
                    price = 1000,
                    status = CafeOrderStatus.READY,
                    orderedAt = LocalDateTime.now(),
                    id = 1
                )

                call.respond(order)
            }
        }
    }
}
