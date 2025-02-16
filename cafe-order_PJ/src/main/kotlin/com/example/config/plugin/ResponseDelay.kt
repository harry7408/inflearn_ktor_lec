package com.example.config.plugin

import io.ktor.server.application.*

// 응답에 0.5초 Delay 를 주는 Custom Plugin
val responseDelayPlugin=
    createApplicationPlugin(name = "ResponseDelayPlugin") {
        onCall {
            Thread.sleep(500)
        }
    }