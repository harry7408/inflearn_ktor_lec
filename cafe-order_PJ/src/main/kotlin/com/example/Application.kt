package com.example

import com.example.config.*
import com.example.config.plugin.configureHttp
import com.example.config.plugin.configureLogging
import com.example.di.configureDependencyInjection
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureHttp()
    configureDatabase()
    configureSerialization()
    configureSession()
    configureSecurity()
    configureDependencyInjection()
    configureRouting()
    configureErrorHandling()
    configureLogging()
}
