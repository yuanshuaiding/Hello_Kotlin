package com.eric.kotlin.kotlinlib.enum

enum class Color1 {
    BLACK, WHITE
}
//需要使用var或val来修饰构造函数里的参数,才会被编译器编译为成员变量,否则只是一个普通的形参
enum class Color2(val rgb: Int) {
    BLACK(0x000000), WHITE(0xffffff)
}

enum class Color3(rgb: Int){
    BLACK(0x000000){
        override val mRgb=0x000000
    };

    open val mRgb:Int=0
}

fun main() {
    Color1.values().map {
        println(it.name)
    }

    Color2.values().map {
        println(it.name + "--->" + it.rgb)
    }
}