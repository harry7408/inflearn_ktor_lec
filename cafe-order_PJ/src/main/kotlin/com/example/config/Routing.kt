package com.example.config

import com.example.route.menuRoute
import com.example.route.orderRoute
import com.example.route.userRoute
import com.example.service.LoginService
import com.example.service.MenuService
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Application.configureRouting() {

    // Koin 을 활용하여 의존성 주입
    // Service Layer 를 두어 Domain
    val cafeMenuService by inject<MenuService>()
    val loginService by inject<LoginService>()

    routing {
        route("/api") {
            // Extension Func으로 Business 로직 별 Route 따로 구성
            // 메뉴 조회
            menuRoute()

            // 사용자 관련
            userRoute()

            // 주문 관련
            orderRoute()

        }

        // React 로 구성된 Web Frontend 실행 (Html 제공 기능)
        // fontend 라는 파일 경로를 찾아 제공
        singlePageApplication {
            useResources=true
            react("frontend")
        }
    }
}
