package com.example.config.plugin

import io.ktor.server.application.*
import io.ktor.server.plugins.doublereceive.*

fun Application.configureHttp() {
    // requestBody 를 여러 번 읽을 수 있는 Plugin
    // (requestBody 는 stream 형태로 1번 읽으면 더이상 소비 불가능)
    install(DoubleReceive)

    // 응답을 지연해서 보내주는 Custom Plugin
    install(responseDelayPlugin)
}