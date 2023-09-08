package com.eric.kotlin.kotlinlib.coroutine.demo

import kotlinx.coroutines.*

fun main() {
    val corroutineScope = CoroutineScope(Dispatchers.Default)
    corroutineScope.launch {
        launch {
            val a=10
            println("first launch start:"+Thread.currentThread().name)
            delay(1000)
            println("first launch end:"+Thread.currentThread().name)
        }
        //切换线程
        withContext(Dispatchers.Unconfined) {
            println("delay 500 start:"+Thread.currentThread().name)
            delay(500)
            println("delay 500 end:"+Thread.currentThread().name)
        }
        launch {
            //切回原线程
            println("second launch:"+Thread.currentThread().name)
        }
        val hi = suspendTest("hello")
        println(hi)
        println("after hi")
    }
    println("main（）函数输出")
    Thread.sleep(2000)
}

suspend fun suspendTest(hi:String): String {
    return withContext(Dispatchers.IO) {
        delay(1000)
        "$hi from suspend"
    }
}