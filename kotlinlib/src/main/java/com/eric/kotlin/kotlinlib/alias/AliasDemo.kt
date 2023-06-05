package com.eric.kotlin.kotlinlib.alias

/**
 * 当某种类型（类，或函数申明）名称太长，想要使用更简短的名称类代替时，可以给这种类型起个“外号”，也叫“别名”。
 *
 * 类型别名不会引入新类型。 它们等效于相应的底层类型。Kotlin 编译器也总是把它视为对应的底层类型。
 */

//举例1：缩短较长的类型申明
typealias KV = LinkedHashMap<String, String>
//举例2：缩短较长的泛型类型申明
typealias KV2<K, V> = HashMap<K, V>

//举例3：为函数申明提供简短名称
typealias opt = (String, Int) -> String


fun main() {
    //直接使用别名创建对象
    val kvs = KV()
    kvs["1"] = "a"
    kvs["2"] = "b"

    val kv2s = KV2<String, Int>()
    kv2s["a"] = 1
    kv2s["b"] = 2

    //使用别名代替函数申明
    fun myStringFun(f: opt): String {
        return f("eric ", 10)
    }

    val str = myStringFun { s, i ->
        s.repeat(i)
    }

    println(kvs)
    println(kv2s)
    println(str)
}