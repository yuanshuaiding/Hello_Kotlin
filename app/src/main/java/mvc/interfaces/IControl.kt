package mvc.interfaces

/**
 *MVC架构中Control示例,用于业务逻辑控制.
 * 处理完逻辑后会更新数据,所以会持有Model的引用.
 */
interface IControl {
    fun setModel(model: IModel)
    fun handleData(s: CharSequence?)
}
