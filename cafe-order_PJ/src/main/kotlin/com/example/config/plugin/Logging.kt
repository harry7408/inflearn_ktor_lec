package com.example.config.plugin

import io.ktor.server.application.*

// Custom 으로 만든 Logging 설정 부분
fun Application.configureLogging() {
    install(MyCallLogging)
}