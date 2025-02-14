package com.example.config


import io.ktor.server.application.*
import org.h2.tools.Server


fun Application.configureDatabase() {
    configureH2()
}


// H2 데이터베이스 동작 설정
fun Application.configureH2() {
    val h2server = Server.createTcpServer("-tcp","-tclAllowOthers","-tcpPort","8001")

    environment.monitor.subscribe(ApplicationStarted) { application ->
        h2server.start()
        application.environment.log.info("H2 Server Started. ${h2server.url}")
    }

    environment.monitor.subscribe(ApplicationStopped) { application ->
        h2server.stop()
        application.environment.log.info("H2 Server Stopped. ${h2server.url}")
    }
}