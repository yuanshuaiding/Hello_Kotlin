package com.eric.kotlin.kotlinlib.delegation

import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * 属性委托指的是一个类的某个属性值不是在类中直接进行定义，而是将其托付给一个代理类，从而实现对该类的属性统一管理。
 *
 * 属性委托语法格式：val/var <属性名>: <类型> by <表达式>
 *
 * by 关键字之后的表达式就是委托, 属性的 get() 方法(以及set() 方法)将被委托给这个对象的 getValue()和 setValue()方法。属性委托不必实现任何接口, 但必须提供 getValue() 函数(对于 var属性,还需要 setValue() 函数)。
 *
 */

class PropertiesDelegationDemo {
    // 成员变量p属于属性委托，其取值和赋值逻辑由委托类MyPropertyDelegate来实现
    var p: String by MyPropertyDelegate()

    // name属性委托交由MyCommonClass的扩展函数实现
    var name: String by MyCommonClass()

    // kotlin内置的开箱即用的标准属性委托1：lazy()函数,此函数用于实现属性的惰性初始化，即只有在第一次访问属性时，才对它进行初始化，其后直接使用第一次的值
    val lazyName: String by lazy {
        println("延迟初始化的属性：lazyName")
        "Hello"
    }

    // kotlin内置的开箱即用的标准属性委托2:Delegates.observable()函数，此函数接受两个值：初始值与修改时处理程序，每当我们给属性赋值时会调用该处理程序
    // 此属性又叫可观察属性（Observable properties）
    var observerName: String by Delegates.observable("old") { property, oldValue, newValue ->
        println("${property.name}的旧值：${oldValue}被修改为新值：${newValue}")
    }

    // kotlin内置的开箱即用的标准属性委托3:Delegates.vetoable()函数，此函数接受两个值：初始值与修改时处理程序，此处理程序判断是否应用新值，每当我们给属性赋值时会调用该处理程序
    // 此属性也叫可观察属性（Observable properties）
    var observerName2: String by Delegates.vetoable("old") { property, oldValue, newValue ->
        val change = newValue.length < 4
        println("${property.name}的旧值：${oldValue}想要被修改为新值：${newValue}，是否可以修改：$change")
        // 相当于observerName2字符串长度最多到3个
        return@vetoable change
    }

    // kotlin内置的开箱即用的标准属性委托4：委托给另一个属性(如注释块里示例1)，一个属性可以把它的 getter 与 setter 委托给另一个属性。这种委托对于顶层和类的属性（成员和扩展）都可用。该委托属性可以为：
    // 1.顶层属性
    // 2.同一个类的成员或扩展属性
    // 3.另一个类的成员或扩展属性
    /*
    //示例1
    var topLevelInt: Int = 0
    class ClassWithDelegate(val anotherClassInt: Int)

    class MyClass(var memberInt: Int, val anotherClassInstance: ClassWithDelegate) {
        var delegatedToMember: Int by this::memberInt
        var delegatedToTopLevel: Int by ::topLevelInt

        val delegatedToAnotherClass: Int by anotherClassInstance::anotherClassInt
    }
    var MyClass.extDelegated: Int by ::topLevelInt

    //示例2：这是很有用的，例如，当想要以一种向后兼容的方式重命名一个属性的时候：引入一个新的属性、 使用 @Deprecated 注解来注解旧的属性、并委托其实现
    class MyClass {
       var newName: Int = 0
       @Deprecated("Use 'newName' instead", ReplaceWith("newName"))
       var oldName: Int by this::newName
    }
     */

    // kotlin内置的开箱即用的标准属性委托5：将属性存储在map里，这经常出现在像解析 JSON 或者执行其他“动态”任务的应用中。 在这种情况下，你可以使用map实例自身作为委托来实现委托属性。
    class User(private val map: Map<String, Any>) {
        val name: String by map
        val age: Int by map
    }

    // kotlin内置的开箱即用的标准属性委托6：局部委托属性，你可以将局部变量声明为委托属性。 例如，你可以使一个局部变量惰性初始化。
    fun example(b: Boolean, f: () -> User) {
        val localLazyUser by lazy {
            println("开始创建localLazyUser")
            f.invoke()
        }

        // 当b为false时，localLazyUser不会被初始化，从而减少对象创建
        if (b && localLazyUser.name.isNotEmpty()) {
            println(localLazyUser)
        }
    }

}

// 自定义委托类
class MyPropertyDelegate {
    // thisRef 就是发起委托的对象,KProperty封装了发起委托的属性的元信息
    operator fun getValue(thisRef: Any, property: KProperty<*>): String {
        return "$thisRef, 这里委托了 ${property.name} 属性"
    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
        println("$thisRef 的 ${property.name} 属性赋值为 $value")
    }
}

// 某些情况，如果已经定义好的某个类无法修改（如第三方SDK里的类），想要这个类也能具备委托属性的能力，则可以借助扩展函数
final class MyCommonClass {
    private var name: String? = ""

    // getName()方法不符合委托类需要getValue()方法的约定
    fun getName(): String {
        return name ?: ""
    }

    fun setName(newName: String?) {
        this.name = newName
    }
}

// 借助扩展函数，使得MyCommonClass也具备委托类的能力
operator fun MyCommonClass.getValue(thisRef: Any, property: KProperty<*>): String {
    return this.getName()
}

operator fun MyCommonClass.setValue(thisRef: Any, property: KProperty<*>, value: String) {
    this.setName(value)
}

fun main() {
    val demo = PropertiesDelegationDemo()
    // 读取属性委托值
    println(demo.p)
    // 设置属性委托值
    demo.p = "hello"

    demo.name = "kotlin"
    println(demo.name)


    // kotlin内置lazy实现的属性委托
    println(demo.lazyName)
    // 第二次不会执行初始化，而是直接取第一次的值
    println(demo.lazyName)

    // 可观察属性1
    println(demo.observerName)
    demo.observerName = "new"
    println(demo.observerName)

    // 可观察属性2
    println(demo.observerName2)
    demo.observerName2 = "new2"
    println(demo.observerName2)
    demo.observerName2 = "hi"
    println(demo.observerName2)

    // 使用map映射属性
    val map = mapOf(
        "name" to "John",
        "age" to 18
    )
    val user = PropertiesDelegationDemo.User(map)
    println(user.name)
    println(user.age)

    // 不被访问的局部委托属性不会被创建
    demo.example(false) {
        PropertiesDelegationDemo.User(
            mapOf(
                "name" to "tom",
                "age" to 20
            )
        )
    }
}