package mvp.interfaces

/**
 * MVC架构中Presenter示例,相当于控制器,用于事件响应和逻辑处理.
 * 响应view事件,并通过Model完成数据处理,最终再展示到VIew,所以需要持有对View和Model的引用.
 */
interface IPresenter {
    fun setView(view: IVeiw)
    fun setModel(model: IModel)
    fun onTextChanged(data: CharSequence)
    fun onClearBtnClicked()
    fun dataHandled(s: CharSequence)
}