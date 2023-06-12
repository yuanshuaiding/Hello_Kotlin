package com.eric.kotlin.kotlinlib.delegation

import org.jetbrains.annotations.TestOnly

interface Base {
    val name: String
        get() = "Base"

    fun fun1()
    fun fun2()
}

class BaseImpl : Base {
    override val name: String = "BaseImpl"
    override fun fun1() {
        println("共有方法1，成员变量name=$name")
    }

    override fun fun2() {
        println("共有方法2，成员变量name=$name")
    }
}

/**
 * 自定义代理类：by 子句的含义是，将传入的对象b（某个具体的BaseImpl对象）保存在MyDelegateClass类的内部，且编译器会自动在此类中生成Base接口的所有方法，同时这些方法的调用都转发给b.
 *
 * 看起来和一般的静态代理一样，只是通过by关键字节省了大量模板代码。也可以对Base类的方法重写，提供日志、拦截等额外功能。
 *
 * 需要注意的是：通过by 完成的代理类，也会把公有成员属性name委托到当前类，但以这种方式重写的成员属性不会在委托对象b的方法中调用 ，委托对象b的方法只能访问其自身对接口成员变量的实现。
 *
 */
class MyDelegateClass(val b: Base) : Base by b {
    override val name: String = "MyDelegateClass"
    fun myFun() {
        println("代理类的独有方法")
    }

    override fun fun1() {
        println("代理类fun1开始执行...")
        //b.fun1()里访问的依然是BaseImpl里的name，而非MyDelegateClass类里覆写的name
        b.fun1()
        println("代理类fun1执行结束")
    }
}

@TestOnly
fun main() {
    val b = BaseImpl()
    val myDel = MyDelegateClass(b)
    myDel.fun1()
    myDel.fun2()
    myDel.myFun()
}