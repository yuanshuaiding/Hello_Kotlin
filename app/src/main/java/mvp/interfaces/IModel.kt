package mvp.interfaces
/**
* MVP架构中Model示例,用于数据获取,更新,存储等.
* 数据更新后需要通过Presenter更新View,所以持有presenter的引用.
*/
interface IModel {
    fun setPresenter(presenter:IPresenter)
    fun handleData(s:CharSequence)
    fun clearData()
}