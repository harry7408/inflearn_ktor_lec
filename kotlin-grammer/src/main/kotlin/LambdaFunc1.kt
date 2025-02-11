package com.example

fun nonLambdaAdd(a: Int, b: Int): Int {
    return a + b
}

// 함수를 변수처럼 활용 가능
val lambdaAdd: (a: Int, b: Int) -> Int =
    // Lambda (마지막 줄이 반환 값)
    { a, b -> a + b }

//이런 형태도 가능
val lambdaAdd2 = { a: Int, b: Int ->
    a + b
}


fun main() {
    val a = 10
    val b = 5

    println(nonLambdaAdd(a, b))

    // invoke 활용
    println(lambdaAdd.invoke(a, b))

    println(lambdaAdd(a, b))
}