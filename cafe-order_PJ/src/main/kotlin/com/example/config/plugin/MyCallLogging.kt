package com.example.config.plugin

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.logging.*
import org.slf4j.event.Level

internal val LOGGER = KtorSimpleLogger("[CallLogging]")

// Log 를 생성하는 CustomLog Plugin
val MyCallLogging =
    createRouteScopedPlugin("MyCallLogging") {
        // 응답을 받았을 때 처리
        onCallRespond { call, body ->
            if (!call.request.uri.startsWith("/api")) return@onCallRespond

            LOGGER
                .atLevel(getLevel(call))
                .log(format(call, body))
        }
    }

fun getLevel(call: ApplicationCall): Level =
    if (call.response.status()?.isSuccess() ?: true) Level.INFO else Level.ERROR

suspend fun format(call: ApplicationCall, body: Any): String {
    val method = call.request.httpMethod.value
    val uri = call.request.uri
    // receiveText 가 suspend Func
    val requestBody = call.receiveText()
        // 불필요한 개행 날리기 위함
        .replace("\r\n", "")
        .replace("\n", "")
        .replace(" ", "")

    // 요청에 대한 정보
    val request = "Request : $method $uri, $requestBody"

    // HTTP Status Code
    val status = "${call.response.status()?.value ?: ""}"

    // 응답에 대한 정보
    val bodyStr = if (body is TextContent) {
        "Response : ${body.text}"
    } else {
        ""
    }

    val log = listOf(request, status, bodyStr).filter {
        it.isNotBlank()
    }.joinToString("\n")

    return "\n" + log
}