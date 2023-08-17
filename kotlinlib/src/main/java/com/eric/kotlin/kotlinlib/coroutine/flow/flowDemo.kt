package com.eric.kotlin.kotlinlib.coroutine.flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.system.measureTimeMillis

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

fun simpleFlowDelay100() = flow {
    for (i in 1..3) {
        println("延迟100ms发射数据:$i")
        delay(100)
        emit(i)
    }
}

suspend fun mockRequest(id: Int): Flow<String> = flow {
    emit("mockRequest onStart:$id")
    delay(200) // 模拟网络请求
    emit("mockRequest onResponse:$id")
}

fun main() = runBlocking { // 启动并发的协程以验证主线程并未阻塞
    println("上下文：[${Thread.currentThread().name}]")
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
    } // fold 与 reduce类似，区别是可以指定一个初始累计值
    val foldTotal = simpleAsFlow().map { it * it }.fold(0) { a, value ->
        a + value
    }
    println("reduce total:$reduceTotal")
    println("fold total:$foldTotal")

    // 流是连续的，默认情况下从上游到下游每个过渡操作符都会处理每个发射出的值然后再交给末端操作符
    (1..5)
        .asFlow()
        .filter {
            println("流是连续的：filter$it")
            it % 2 == 0
        }
        .map {
            println("流是连续的：map$it")
            // 转换为字符串
            "String $it"
        }
        .collect {
            println("流是连续的：collect$it")
        }

    // 流的上下文保存：流的收集总是在调用协程的上下文中发生，默认的，flow { ... } 构建器中的代码运行在相应流的收集器提供的上下文中
    withContext(Dispatchers.IO) {
        flow {
            println("流的上下文保存：start flow in thread [${Thread.currentThread().name}]")
            for (i in 1..3) {
                emit(i)
            }
        }.collect {
            println("流的上下文保存：collect $it in thread [${Thread.currentThread().name}]")
        }
    }

    // 如果非要让流构建器中的代码和收集不在同一个上下文（线程）中，只能通过flowOn函数来更改流的发射的上下文
    flow {
        println("流的上下文修改：start flow in thread [${Thread.currentThread().name}]")
        for (i in 1..3) {
            Thread.sleep(1000)
            emit(i)
        }
    }.flowOn(Dispatchers.IO).collect {
        println("流的上下文修改：collect $it in thread [${Thread.currentThread().name}]")
    }

    // 流的缓冲:使用buffer操作符
    var time = measureTimeMillis {
        simpleFlowDelay100().collect {
            // 模拟复杂耗时操作
            delay(300)
            println("流的缓冲1：$it")
        }
    }
    println("流的缓冲1:未使用缓冲耗时:$time")

    time = measureTimeMillis {
        simpleFlowDelay100().buffer().collect {
            // 模拟复杂耗时操作
            delay(300)
            println("流的缓冲2：$it")
        }
    }
    println("流的缓冲2:使用缓冲耗时:$time")

    // 流的结果合并：使用conflate，处理流所代表的部分操作结果或操作状态更新时，可能没有必要处理每个值，而是只处理最新的那个，一般是处理比发射更慢时可以优化性能
    time = measureTimeMillis {
        simpleFlowDelay100().conflate().collect {
            // 模拟复杂耗时操作
            delay(300)
            println("流的合并：$it")
        }
    }
    println("流的合并:使用合并的耗时:$time")

    // 只处理最新值:collectLatest,
    time = measureTimeMillis {
        simpleFlowDelay100().collectLatest {
            println("只处理最新值：接收到的值 $it")
            // 模拟复杂耗时操作
            delay(300)
            println("只处理最新值：实际处理的值 $it")
        }
    }
    println("只处理最新值:耗时:$time")

    // 组合两个流：zip，不同于combine的是，当其中一个流结束时，另外的Flow也会调用cancel，生成的流完成
    val f1 = (1..5).asFlow().onEach { delay(200) }
    val f2 = flowOf("a", "b", "c").onEach { delay(300) }
    time = measureTimeMillis {
        f1.zip(f2) { a, b ->
            "zip结果1：$a->$b"
        }.collect {
            println(it)
        }
    }
    println("zip结果1:耗时:$time")

    time = measureTimeMillis {
        f2.zip(f1) { a, b ->
            "zip结果2：$a->$b"
        }.collect {
            println(it)
        }
    }
    println("zip结果2:耗时:$time")

    time = System.currentTimeMillis()

    // 组合流并响应每个流的最新值:combine
    f1.combine(f2) { a, b ->
        "combine结果：$a->$b"
    }.collect {
        println(it + "，耗时：${System.currentTimeMillis() - time}")
    }

    // 顺序展平流：操作发射结果为流的流，将嵌套流按顺序展平为单一流，如flatMapConcat
    simpleAsFlow().onEach { delay(100) }.map {
        mockRequest(it)
    }.collect {
        // 嵌套流，需要展平为单个流处理
        it.collect { request ->
            println(request)
        }
    }

    // 上面的嵌套流处理可以使用展平流操作符flatConcat简化处理
    time = measureTimeMillis {
        simpleAsFlow()
            .onEach { delay(100) }
            .map {
                mockRequest(it)
            }
            .flattenConcat() // 以顺序方式将给定的流展开为单个流
            .collect { request ->
                // flattenConcat处理过的嵌套流，已经是单个流了
                println(request)
            }
    }
    println("flattenConcat耗时：$time")

    // 上面的嵌套流处理可以进一步使用展平流操作符flatMapConcat简化处理(相当于 map + flattenConcat)
    time = measureTimeMillis {
        simpleAsFlow()
            .onEach { delay(100) }
            .flatMapConcat {
                mockRequest(it)
            }
            .collect { request ->
                // flatMapConcat处理过的嵌套流，已经是单个流了
                println(request)
            }
    }
    println("flatMapConcat耗时：$time")

    // 并发展平流：flatMapMerge，不用等某个嵌套流发送完成才进行下个嵌套流，而是所有嵌套的流并发执行,可指定并发数量
    time = measureTimeMillis {
        simpleAsFlow()
            .onEach { delay(100) }
            .flatMapMerge {
                mockRequest(it)
            }
            .collect { request ->
                // flatMapConcat处理过的嵌套流，已经是单个流了
                println(request)
            }
    }
    println("flatMapMerge耗时：$time")

    // 只收集最新嵌套流：flatMapLatest,在发出新流后立即取消先前流的收集
    time = measureTimeMillis {
        simpleAsFlow()
            .onEach { delay(100) }
            .flatMapLatest {
                mockRequest(it)
            }
            .collect { request ->
                // flatMapConcat处理过的嵌套流，已经是单个流了
                println(request)
            }
    }
    println("flatMapLatest耗时：$time")
}