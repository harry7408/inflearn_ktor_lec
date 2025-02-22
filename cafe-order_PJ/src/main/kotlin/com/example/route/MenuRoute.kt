package com.example.route

import com.example.service.MenuService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Route.menuRoute() { // this -> Route

    val cafeMenuService by inject<MenuService>()

    get("/menus") {
        val list = cafeMenuService.findAll()
        // 응답할 때 사용
        call.respond(list)
    }
}