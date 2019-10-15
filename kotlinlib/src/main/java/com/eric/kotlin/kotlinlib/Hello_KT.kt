package com.eric.kotlin.kotlinlib

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2018/11/22
 * desc   : hello world for kotlin
 * version: 1.0
 */

//kotlin 1.3之后main入口方法可以省略参数,666
fun main() {
    println("hello world from kotlin")
    val hello_KT = Hello_KT()
    hello_KT.hi()
}

class Hello_KT {
    fun hi() {
        println(Hello_KT::class.simpleName)
    }
}

//fun main(args: Array<String>) {
//    args.forEach(::println)
//
//    args.map(::println)
//}

fun main(vararg args: String) {
    args.flatMap { it.split("l") }.map{
        println("$it ${it.plus("666")}")
    }
}
