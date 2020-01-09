package com.eric.kotlin.kotlinlib.visibility.pack1

class MyClass private constructor(index: String) {

    companion object {
        fun getInstance(s: String, type: String = "default"): MyClass {
            return MyClass(s, type)
        }

        init {
            println("执行MyClass伴生对象里的init代码块")
        }
    }

    private constructor(index: String, type: String) : this(index) {
        println("创建MyClass,index=$index,type=$type")
    }

    init {
        println("创建MyClass,index=$index")
    }
}