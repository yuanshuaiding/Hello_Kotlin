package com.eric.kotlin.kotlinlib.coroutine.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.runBlocking
import java.lang.RuntimeException

/**
 * 当运算符中的发射器或代码抛出异常时，流收集可以带有异常的完成。 有几种处理异常的方法。
 */

fun simpleFlow(): Flow<Int> = flow {
    for (i in 1..3) {
        println("Emitting1 $i")
        emit(i) // 发射下一个值
    }
}


fun main()= runBlocking {
    //使用 try/catch 块来处理异常(捕获在发射器或任何过渡或末端操作符中发生的任何异常)
    try {
        simpleFlow().collect{
            println(it)
            check(it<=1){
                "Exception value1:$it"
            }
        }
    }catch (e:Exception){
        println("在末端操作符 collect 中捕获了异常: $e")
    }

    /*
     * 使用 catch 操作符来捕获并处理异常（仅捕获上游异常，此操作后面的异常无法捕获），保留此异常的透明性并允许封装它的异常处理：
     * - 可以使用 throw 重新抛出异常。
     * - 可以使用 catch 代码块中的 emit 将异常转换为值发射出去。
     * - 可以将异常忽略，或用日志打印，或使用一些其他代码处理它。
     */
    flow {
        for (i in 1..3){
            println("Emitting2 $i")
            emit(i)
        }
    }.map {
        check(it<2){"Exception value2:$it"}
        "string $it"
    }.catch { e->
        //将异常包装成字符串发射或其它处理
        emit("在过渡操作符 map 中捕获了异常: $e")
    }.collect{
        println(it)
    }

    /*
     * onComplete 操作符来捕获，但不能处理异常，异常会继续流向下游。其 lambda 表达式的可空参数 Throwable 可以用于确定流收集是正常完成还是有异常发生.
     */

    flow {
        emit(1)
        delay(100)
        throw RuntimeException()
        emit(2)
    }.onCompletion {cause->
        println("onCompletion捕获异常1:$cause")
    }.catch { ex->
        println("catch 处理异常1：$ex")
    }.collect{
        println(it)
    }

    /**
     * 与 catch 操作符的另一个不同点是onComplete能观察到所有异常并且仅在上游流成功完成（没有取消或失败）的情况下接收一个 null 异常.
     * 如下代码所示，虽然下游colllect操作符发生了异常，依然能被前面声明的onComplete捕获，但catch操作符却不行。
     */
    flow {
        emit(1)
        delay(100)
        emit(2)
    }.onCompletion {cause->
        println("onCompletion捕获异常2:$cause")
    }.catch { ex->
        println("catch 处理异常2：$ex")
    }.collect{
        check(it<2)
        println(it)
    }
    delay(100)
    println("main ended!")
}