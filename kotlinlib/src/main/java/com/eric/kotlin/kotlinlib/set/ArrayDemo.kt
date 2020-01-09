package com.eric.kotlin.kotlinlib.set

//数组
//练习题
//分别用 Array、IntArray、List 实现 「保存 1-100_000 的数字，并求出这些数字的平均值」，打印出这三种数据结构的执行时间。
fun main() {
    val maxSize = 100_000
    val intArr = IntArray(maxSize) { i -> i + 1 }
    val array = Array(maxSize) { i -> i.inc() }
    val listArr = List(maxSize) { it + 1 }

    var start = System.currentTimeMillis()
    var total = 0L
    for (i in intArr) {
        total += i
    }
    var avg = total / intArr.size.toFloat()
    println("耗时:${System.currentTimeMillis() - start},平均值:$avg")

    start = System.currentTimeMillis()
    total = 0L
    array.forEach { total += it }
    avg = total / array.size.toFloat()
    println("耗时:${System.currentTimeMillis() - start},平均值:$avg")

    start = System.currentTimeMillis()
    total = 0L
    listArr.forEach { total += it }
    avg = total / listArr.size.toFloat()
    println("耗时:${System.currentTimeMillis() - start},平均值:$avg")
}