package com.eric.kotlin.kotlinlib

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2018/11/22
 * desc   : hello world for kotlin
 * version: 1.0
 */

//kotlin 1.3之后main入口方法可以省略参数,666
fun main() {
    println("hello world from kotlin")
    val hello_KT = Hello_KT()
    hello_KT.hi()
}

class Hello_KT {
    fun hi() {
        println(Hello_KT::class.simpleName)
    }
}

//fun main(args: Array<String>) {
//    args.forEach(::println)
//
//    args.map(::println)
//}

fun main(vararg args: String) {
    args.flatMap { it.split("l") }.map {
        println("$it ${it.plus("666")}")
    }

    var room: Room? = null
    val roomNumber = room?.number
    println("the room is number ${roomNumber} on floor ${room?.floor}")
    /*
    *   Kotlin 分为两类相等性：
    *
    *   构成相等使用 == 运算符，并调用 equals() 来确定两个实例是否相等。
    *   引用相等使用 === 运算符，以检查两个引用是否指向同一对象。
    *   数据类主构造函数中定义的属性将被用于检查构成相等性。
    */

    val room1 = Room("1", "0")
    val room2 = Room("1", "0")

    val rooms = ArrayList<Room>()
    rooms.add(room1)
    rooms.add(room2)

    println("room1==room2 : ${room1 == room2}")
    println("room1===room2 : ${room1 === room2}")

    println(room1.toString())
    println(room1.hashCode())
    println(room2.toString())
    println(room2.hashCode())

    RoomUtil.setRooms(rooms)
    println(RoomUtil.roomInfos)


    room = Room("0", "0")

    room.run {
        floor = "100"
        number = "1010"
    }

    println(room)


    //let:场景一: 最常用的场景就是使用let函数处理需要针对一个可null的对象统一做判空处理。
    println("let 进行统一非空判断")
    room?.let {
        println(it.number)
        println(it.floor)
    }
    //let:场景二:当你想定义一个只存在特定域且不能超出此域的变量的时候可以使用它，它对编写自包含(self-contained)的代码尤其有用，从而不会发生变量泄漏(leaking out)
    println("let 编写特定作用域代码")
    Room("102", "10").let {
        //此时Room对象只能在此处使用
        println(it.number)
        println(it.floor)
        it.number = "1020"
    }
}

data class Room(var number: String, var floor: String)

object RoomUtil {
    private lateinit var rooms: ArrayList<Room>

    fun setRooms(rooms: ArrayList<Room>) {
        this.rooms = rooms
    }

    val roomInfos: String
        get() {
            var strs = ""
            rooms.map { room ->
                val floor = room.floor
                val number = room.number
                strs += ("room info:$floor----->$number\n")
            }
            return strs
        }
}

class Myclass {
    var exception = run { Exception() }
    fun printExceptionMsg(ex: Exception?) {
        ex?.run { println(message) }
    }
}