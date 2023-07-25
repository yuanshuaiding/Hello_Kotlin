package com.eric.kotlin.kotlinlib.coroutine.cancel

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * 在这个示例中，我们创建了一个协程  job  来打印一些信息。在  repeat  循环中，协程会不断地睡眠并打印信息，同时检查协程的状态。
 * 如果协程被取消，就会退出循环并返回。在  main  函数中，我们让主线程等待一段时间后再取消协程，并在取消之前等待它完成。
 * 这个示例展示了协作取消的基本原则，也告诉我们如何在需要时检查协程的状态。
 * 使用这种方式进行协作取消可以确保协程取消的准确性，并保证在取消时不会造成资源的浪费。
 * 需要注意的是，在协程内部使用  isActive  进行判断，如果判断条件不合理，可能会影响协程的执行效率和质量。
 */
fun main(array: Array<String>) = runBlocking {
   val job = launch {
       repeat(1000) { i ->
           println("I'm sleeping $i ...")
           delay(500L)
           if (!isActive) {
               println("I'm cancelled")
               return@launch
           }
       }
   }
   delay(1300L)
   println("main: I'm tired of waiting!")
   job.cancelAndJoin()
   println("main: Now I can quit.")

   //普通耗时函数没有suspend，也就没有协程所需的状态判断，所以协程内的这种函数执行时，取消协程时协程不会立刻取消，需要等普通耗时函数执行完成。
   val job2 = launch {
       normalFun()
   }
   val job3 = launch {
       suspendFun()
   }
   delay(1000)
   job2.cancel()
   job3.cancel()
}

fun normalFun() {
   println("普通耗时函数开始执行......")
   Thread.sleep(2000)
   println("普通耗时函数执行完成！")
}

suspend fun suspendFun() {
   println("可挂起耗时函数开始执行......")
   delay(2000)
   println("可挂起耗时函数执行完成！")
}