package com.eric.kotlin.kotlinlib.objectExpressions

/**
 * 使用object关键字后面跟一个名称，就称为对象声明，这是kotlin创建单例的一种方式，线程安全且在首次访问时创建单例对象;
 * 如需引用该对象，我们直接使用其名称即可.
 *
 * 注意：对象声明不能在局部作用域（即直接嵌套在函数内部），但是它们可以嵌套到其他对象声明或非内部类中
 */

object MySingleton {
    fun hi() {
        println("hello kotlin!")
    }
}

class MyObjClass {
    object MyObj {
        fun hello() {
            println("hello from MyObj")
        }
    }

    //类内部的对象声明可以使用companion来标记，同时可以省略名称
//    companion object CompanionObj{
//        fun hello(){
//            println("hello from companion object")
//        }
//    }
    companion object {
        //请注意，即使伴生对象的成员看起来像其他语言的静态成员，在运行时他们仍然是真实对象的实例成员
        fun hello() {
            println("hello from companion object")
        }

        //使用 @JvmStatic 注解，你可以将伴生对象的成员生成为真正的静态方法和字段
        @JvmStatic
        fun helloJava(){
            println("helloJava from companion object")
        }
    }

    inner class InnerClass {
        //内部类里不允许出现对象声明
//        object InnerObj{
//
//        }
    }
}

fun main() {
    //kotlin直接使用对象声明的名称，java需要使用名称.INSTANCE来访问此单例对象
    MySingleton.hi()
    MyObjClass.MyObj.hello()
    //伴生对象的成员可通过只使用类名作为限定符来调用
    MyObjClass.hello()
}

