package com.example

// Extension Func : 사용자 정의 함수를 추가하기 용이
// (Class 확장이 쉽지만 남용하지 말 것)
// 활용 예 : FlowBinding, RxBinding ...
fun String.printHello() {
    // this는 String 인스턴스를 의미
    println("hello $this")
}

fun List<Int>.removeEven(): List<Int> {
    return this.filter { it % 2 != 0 }
}

fun main() {
    val str="world"
    // hello world
    str.printHello()

    val list= listOf(1,2,3,4,5)
    // [1,3,5]
    println(list.removeEven())
}