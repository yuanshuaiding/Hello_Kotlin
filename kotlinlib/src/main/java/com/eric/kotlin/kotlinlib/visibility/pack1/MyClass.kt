package com.eric.kotlin.kotlinlib.visibility.pack1

class MyClass {


    private var index: String = ""

    companion object {
        fun getInstance(s: String, type: String = "default"): MyClass {
            return MyClass(s, type)
        }

        init {
            println("执行MyClass伴生对象里的init代码块")
        }
    }

    private constructor() {
        println("创建MyClass,执行构造函数constructor(),index=$index")
    }

    private constructor(index: String):this() {
        this.index = index
        println("创建MyClass,执行构造函数constructor(index: String),index=$index")
    }

    private constructor(index: String, type: String) : this(index) {
        println("创建MyClass,执行构造函数constructor(index: String, type: String),index=$index,type=$type")
    }

    init {
        println("创建MyClass,执行init代码块,index=$index")
    }

    init {
        println("创建MyClass,执行init代码块2,index=$index")
    }
}