package com.eric.kotlin.kotlinlib.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun main() {
    val corroutineScope = CoroutineScope(Dispatchers.Default)
    corroutineScope.launch {
        withContext(Dispatchers.Main){
            //切换至主线程
            println(Thread.currentThread().name)
        }
        launch {
            //在主线程
            println(Thread.currentThread().name)
        }
    }
    Thread.sleep(1000)
}