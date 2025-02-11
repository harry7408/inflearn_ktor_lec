package com.example

fun multiple2(block: () -> Int): Int {
    // block.invoke() 도 가능
    return block() * 2
}

fun main() {
    val will5: () -> Int = { 5 }

    println(multiple2(will5))

    // 맨 마지막 인자로 전달되는 람다 (Postfix 표기법)
    // build.gradle 에서 보이는 형태의 코드
    // => 매우 많이 활용된다 (코드의 가독성이 좋아짐)
    println(
        multiple2 {
            10
        }
    )
}