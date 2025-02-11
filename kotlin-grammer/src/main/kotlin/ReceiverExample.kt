package com.example

data class Configuration(
    var host: String="",
    var port : Int= 0,
)

// 람다식이 Receiver 형태로 되어있음
fun configure(block : Configuration.() -> Unit) : Configuration {
    // todo
    val config= Configuration()
    // 멤버 함수처럼 사용할 수 있다
    // block(config), block.invoke(config) 도 가능
    config.block()
    return config
}


fun main() {
    // this 를 input 으로 받고 있다
    val config= configure {
        host="localhost"
        port=8080
    }

    println(config)
}