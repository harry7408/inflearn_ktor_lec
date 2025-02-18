package com.example.config

import com.example.service.LoginService
import com.example.service.MenuService
import com.example.shared.CafeOrderStatus
import com.example.shared.CafeUserRole
import com.example.shared.dto.OrderDto
import com.example.shared.dto.UserDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject
import java.time.LocalDateTime


fun Application.configureRouting() {

    // Koin 을 활용하여 의존성 주입
    // Service Layer 를 두어 Domain
    val cafeMenuService by inject<MenuService>()
    val loginService by inject<LoginService>()

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

            // 인증된 사용자의 정보 표시 (세션에서 정보를 가져오면 된다)
            get("/me") {
                val user = call.sessions.get<AuthenticatedUser>() ?: AuthenticatedUser.none()
                call.respond(user)
            }

            // 닉네임, Password 를 받아 검증
            post("/login") {
                val user = call.receive< UserDto.LoginRequest>()
                loginService.login(user, call.sessions)
                call.respond(HttpStatusCode.OK)
            }

            // 닉네임, Password 를 받아 회원여부 검증
            post("/signup") {
                val user=call.receive<UserDto.LoginRequest>()
                loginService.signup(user,call.sessions)
                call.respond(HttpStatusCode.OK)
            }

            // 세션과 쿠키 만료하기
            post("/logout") {
                loginService.logout(call.sessions)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
