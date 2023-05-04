package com.eric.kotlin.kotlinlib.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

//测试协程同步问题,此处期望sum的运行结果为20000
//fun main(args: Array<String>) = runBlocking {
//    var sum = 0
//    withContext(Dispatchers.IO) {
//        repeat(10000) {
//            launch { sum++ }
//            launch { sum++ }
//        }
//    }
//    //输出的结果往往不到20000，存在同步问题
//    println("sum:$sum")
//
//
//    var sum1 = 0
//
//    //使用synchronized函数保证共享操作对象sum1的同步性，使用共同的父协程对象this@withContext作为锁
//    withContext(Dispatchers.IO) {
//        repeat(10000) {
//            launch {
//                synchronized(this@withContext) {
//                    sum1++
//                }
//            }
//
//            launch {
//                synchronized(this@withContext) {
//                    sum1++
//                }
//            }
//        }
//    }
//
//    println("sum1:$sum1")
//
//    var sum2 = 0
//
//    //使用kotlin推荐的同步方式：Mutex类
//    val mutex = Mutex()
//    withContext(Dispatchers.IO) {
//        repeat(10000) {
//            launch {
//                mutex.withLock {
//                    sum2++
//                }
//            }
//
//            launch {
//                mutex.withLock {
//                    sum2++
//                }
//            }
//        }
//    }
//
//    println("sum2:$sum2")
//}