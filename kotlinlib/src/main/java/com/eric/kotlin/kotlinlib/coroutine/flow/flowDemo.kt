package com.eric.kotlin.kotlinlib.coroutine.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull

/**
 * 使用 Flow 的代码与先前示例的下述区别：
 *
 * ·Flow 类型的构建器函数名为 flow。
 * ·flow { ... } 构建块中的代码可以挂起。
 * ·函数 simple 不再标有 suspend 修饰符。
 * ·流使用 emit 函数 发射 值。
 * ·流使用 collect 函数 收集 值。
 */
fun simple(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(1000)
        emit(i)
    }
}

// 其它流构建器：flowOf{}
fun simpleFlowOf(): Flow<Int> = flowOf(1, 2, 3)

// 其它流构建器：asFlow()
fun simpleAsFlow(): Flow<Int> = (1..3).asFlow()

// 模仿长时间运行的异步工作
suspend fun performRequest(request: Int): String {
    delay(1000)
    return "response $request"
}

fun numbers(): Flow<Int> = flow {
    try {
        emit(1)
        emit(2)
        println("This line will not execute")
        emit(3)
    } finally {
        println("Finally in numbers")
    }
}

fun main() = runBlocking {
    // 启动并发的协程以验证主线程并未阻塞
    launch {
        for (k in 1..3) {
            println("I'm not blocked $k")
            delay(1000)
        }
    }
    simple().collect {
        println(it)
    }

    // 流采用与协程同样的协作取消
    withTimeoutOrNull(2500) {
        simple().collect {
            println(it)
        }
    }
    println("Done")

    // 过渡流操作符：map/filter,过渡操作符应用于上游流，并返回下游流。 这些操作符也是冷操作符,操作符本身不是挂起函数,操作符中的代码可以调用挂起函数.
    simpleAsFlow().map {
        performRequest(it)
    }.collect {
        println(it)
    }

    simpleAsFlow().filter {
        it % 2 == 0
    }.collect {
        println("filter:$it")
    }

    // 转换操作符: transform,它可以用来模仿简单的转换，例如 map 与 filter，以及实施更复杂的转换。 使用 transform 操作符，我们可以 发射 任意值任意次。
    simpleAsFlow().transform { request ->
        // 此处每次转换都会提交两次数据用于collect
        emit("Making request $request")
        emit(performRequest(request))
    }.collect { response ->
        println(response)
    }

    // 限长过渡操作符（例如 take）在流触及相应限制的时候会将它的执行取消。协程中的取消操作总是通过抛出异常来执行
    numbers().take(2).collect {
        println("take:$it")
    }

    // 末端流操作符:collect,toList,toSet,first,single,reduce,fold等
    val list = simpleAsFlow().toList()
    for (i in list) {
        println("list:$i")
    }

    val reduceTotal = simpleAsFlow().map { it * it }.reduce { a, b ->
        a + b
    }
    //fold 与 reduce类似，区别是可以指定一个初始累计值
    val foldTotal = simpleAsFlow().map { it * it }.fold(0) { a, value ->
        a + value
    }
    println("reduce total:$reduceTotal")
    println("fold total:$foldTotal")


}