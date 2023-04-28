package com.eric.kotlin.kotlinlib.apply_run_let_also_with

fun main() {
//    apply
    val tom = Test("tom", 10)
    tom.age = 20
    tom.name = "tommy"

    //上面的赋值可以简化为
    tom.apply {
        this.age = 20
        name = "tommy"//this可以省略
    }
    //apply返回值是自身,所以此处可以使用toString方法
    val tomStr=tom.apply {
        age = 20
        name = "tommy"
    }.toString()

    println(tomStr)

//    run
    tom.run {  }

}

data class Test(var name: String, var age: Int)