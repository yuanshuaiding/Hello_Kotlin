package com.eric.kotlin.kotlinlib.objectExpressions

/**
 * 对象表达式【使用object表达式和匿名类的方式创建对象均称为对象表达式】：
 * 1.直接使用object表达式：多用于临时的，一次性的创建一个对象，而无需复用它，你的所有属性，方法等都在其后花括号内声明；
 * 2.带超类（继承其它类或实现接口）的object表达式：object后面用冒号“:”分割超类，超类如果是class类型，需要调用构造函数，如果是interface则只需接口名，多个超类间使用逗号“,”分割
 * 3.使用匿名对象作为返回：需要区分private、local对象与public对象，前两者可以访问其内部对象，可以视作一个class类型，而public或inline修饰的话则只能视作Any对象，无法访问对象内部各种属性与方法
 * 4.对象表达式中的代码可以访问来自包含它的作用域的变量
 */


open class UserEvent {
    open val name = "nobody"
    open fun onClick() {
        println("${name}点击！")
    }

    fun onLongClick() {
        println("${name}长按！")
    }
}

interface UserClickListener {
    fun onClick()

    fun onDoubleClick()
}

interface A {
    fun funA()
}

interface B {
    fun funB()
}

class MyWindow {
    var listener: UserClickListener? = null
}

class MyClass {
    // 私有函数，所以其返回类型是匿名对象类型
    private fun getObject() = object {
        val x: String = "x"
    }

    // 公有函数，所以其返回类型是 Any
    fun getObjectPublic() = object {
        val x: String = "x"
    }

    fun getObjectPublicA() = object : A {
        val x: String = "x"
        override fun funA() {
            println("getObjectPublicA funA()执行")
        }
    }

    //有多个超类的对象表达式作为public函数的返回值，需要明确指定类型，否则编译错误：Right-hand side has anonymous type. Please specify type explicitly
    fun getObjectPublicB(): B = object : A, B {
        val x: String = "x"
        override fun funA() {
            println("getObjectPublicB funA()执行")
        }

        override fun funB() {
            println("getObjectPublicB funB()执行")
        }
    }

    fun printMyClass() {
        println("getObject().x=${getObject().x}")
        //编译错误，getObjectPublic()函数使用public修饰，返回值类型是Any类型
        //println("getObject().x=${getObjectPublic().x}")
        getObjectPublicA().funA()
        //编译错误，getObjectPublicA()函数使用public修饰，返回值类型是A类型，无法访问内部属性x
        //println("getObjectPublicA().x=${getObjectPublicA().x}")

        getObjectPublicB().funB()
        //编译错误，getObjectPublicB()函数使用public修饰，且指定返回值类型是B类型，无法访问内部属性x
        //println("getObjectPublicB().x=${getObjectPublicB().x}")
    }
}

fun main() {
    //1. 直接使用object关键字创建对象
    val obj = object {
        val hello = "Hello "
        val world = "world!"
        override fun toString(): String {
            return "$hello$world"
        }
    }

    println(obj)

    println("obj.hello=${obj.hello}")

    //2. 带超类的object表达式（object关键字后用“:”分割，后面跟被继承的类或接口，多个被继承者使用“,”分割）
    //继承父类UserEvent的对象表达式
    val obj2 = object : UserEvent() {
        override val name: String = "eric"

        override fun onClick() {
            println("${name}点击啦！！！")
        }
    }

    //实现接口的对象表达式
    val obj3 = object : UserClickListener {
        override fun onClick() {
            println("单击事件")
        }

        override fun onDoubleClick() {
            println("双击事件")
        }
    }

    //多个超类，如果存在相同的函数签名，则优先使用带实现的，如果多个超类使用了相同的函数签名，则需要重新实现以解决冲突
    val obj4 = object : UserClickListener, UserEvent() {
        override fun onDoubleClick() {
            println("${name}双击666！！！")
        }
    }

    obj2.onClick()
    obj2.onLongClick()

    obj3.onClick()
    obj3.onDoubleClick()

    obj4.onClick()
    obj4.onDoubleClick()
    obj4.onLongClick()

    //3.1 local,private修饰的方法或属性且非inline，使用内部方法与属性可以正常访问
    //local函数，可以访问匿名对象返回值的x属性
    fun getLocalVarFun() = object {
        val x: String = "local x"
    }
    println("getLocalVarFun().x=${getLocalVarFun().x}")

    //3.2 MyClass内部的private方法使用匿名对象返回值，也可以通过此函数访问匿名对象里的x属性，public或private inline修饰的则不行
    val myClass = MyClass()
    myClass.printMyClass()

    //4 对象表达式中的代码可以访问来自包含它的作用域的变量
    var clickCountOut = 0
    fun countClicks(win: MyWindow) {
        var clickCount = 0
        win.listener = object : UserClickListener {
            override fun onClick() {
                clickCount++
                clickCountOut++
            }

            override fun onDoubleClick() {
                clickCount++
                clickCountOut++
            }
        }

    }
}

