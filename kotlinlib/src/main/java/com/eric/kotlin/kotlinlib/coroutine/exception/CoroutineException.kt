package com.eric.kotlin.kotlinlib.coroutine

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * 在传统的原生的异常处理中，我们处理异常无在乎是这两种：
 *
 * tryCatch()；
 *
 * Thread.setDefaultUncaughtExceptionHandler()；
 *
 * 后者常用于非主线程的保底，前者用于几乎任何位置。
 * 因为协程底层也是使用的java线程模型，所以上述的方式，在协程的异常处理中，同样有效.
 *
 * 除了常规方式，官方建议我们使用 CoroutineExceptionHandler 去处理协程中异常，或者作为协程异常的保底手段。
 */

fun main(args: Array<String>) {
    runBlocking {
        val supervisorJob = SupervisorJob()
        launch {
            //常规方式1
            try {
                val num = 1 / 0
                println(num)
            } catch (e: Exception) {
                println("常规方式1: $e")
            }
        }

        launch {
            //常规方式2
            kotlin.runCatching {
                val num = 1 / 0
                println(num)
                num
            }.onSuccess {
                println("onSuccess:$it")
            }.onFailure {
                println("常规方式2: $it")
            }
        }

        //官方建议：CoroutineExceptionHandler
        val handler = CoroutineExceptionHandler { _, throwable ->
            println("拦截到异常：$throwable")
        }

        val scope = CoroutineScope(supervisorJob + handler)
        scope.launch {
            val num = 1 / 0
            println(num)
        }

        /*
         * 在下面的示例中，我们创建了三个任务并发执行，其中任务2在执行1500ms后被取消了。
         * 由于我们使用了SupervisorJob来创建协程，因此即使任务2失败了，任务1和任务3仍然可以继续执行。
         * 最后，我们使用 supervisorJob.children.forEach { it.join() } 等待所有任务结束，输出"All tasks completed"
         */

        val job1 = scope.launch {
            try {
                println("Task 1 starts")
                delay(1000)
                println("Task 1 ends")
            } catch (e: Exception) {
                println("Task 1 failed: $e")
            }
        }

        val job2 = scope.launch {
            try {
                println("Task 2 starts")
                delay(2000)
                println("Task 2 ends")
            } catch (e: Exception) {
                println("Task 2 failed: $e")
            }
        }

        val job3 = scope.launch {
            try {
                println("Task 3 starts")
                delay(3000)
                println("Task 3 ends")
            } catch (e: Exception) {
                println("Task 3 failed: $e")
            }
        }

        delay(1500)
        job2.cancel() // 由于我们使用了SupervisorJob来创建协程，因此即使任务2失败了，任务1和任务3仍然可以继续执行

        supervisorJob.children.forEach { it.join() } // 等待任务结束
        println("All tasks completed")
    }

}