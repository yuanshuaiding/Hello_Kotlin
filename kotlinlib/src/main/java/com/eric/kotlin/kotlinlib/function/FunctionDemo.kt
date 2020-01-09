package com.eric.kotlin.kotlinlib.function

fun main() {
    //1.函数即默认值
    test("tom")
    //如果一个有默认值参数在一个无默认值的参数之前，那么该无默认值的参数只能通过使用[命名参数]的方式赋值
    test1("tom1", sex = "男")
    test1("tomm1")
    test2(name = "tom2")

    //2.高阶函数(函数的参数或返回值是函数类型)
    val ret = hignlevelFun("eric", fun String.(times: Int): String {
        return repeat(times)
    })
    println(ret)



}

//普通函数定义(参数默认是val的)
fun test(name: String, age: Int = 18) {
    println("name=$name,age=$age")
}

//带默认值的普通函数
fun test1(name: String, age: Int = 18, sex: String? = null) {
    println("name=$name,age=$age,sex=${sex ?: "unkown"}")
}

fun test2(age: Int = 18, name: String) {
    println("name=$name,age=$age")
}

//高阶函数定义
fun hignlevelFun(name: String?, f: String.(Int) -> String): String {
    return name?.let {
        f(name, 10)
    } ?: ""
}