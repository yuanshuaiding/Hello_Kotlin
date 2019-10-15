package mvc.interfaces

/**
 * MVC架构中Model示例,用于数据获取,更新,存储等.
 * 数据更新后需要更新View,所以需要持有对View的引用.
 */
interface IModel {
    fun setView(view: IView)
    fun setData(result: CharSequence?)
}