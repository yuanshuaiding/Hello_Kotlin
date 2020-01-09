package com.eric.kotlin.kotlinlib.visibility

import com.eric.kotlin.kotlinlib.visibility.pack1.MyClass

fun main() {
    //创建一个 Kotlin 类，这个类需要禁止外部通过构造器创建实例，并提供至少一种实例化方式。
    MyClass.getInstance("1")
}