package mvc.interfaces

/**
 *MVC架构中View示例,用于展示数据,及响应用户操作(点击,触摸,长按等).
 * 用户操作后需要经过Control进行逻辑处理,所以会持有Control的引用.
 */
interface IView {
    fun setControl(control: IControl)
    fun showData(data: CharSequence?)
}