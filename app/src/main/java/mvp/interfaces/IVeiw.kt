package mvp.interfaces
/**
 * MVP架构中View示例,用于数据展示,接收用户操作
 * 操作后需要通过presenter处理,所以需要持有对Presenter的引用.
 */
interface IVeiw {
    fun setPresenter(presenter:IPresenter)
    fun dataLoading()
    fun showResultView(s: CharSequence)
    fun clearEditTextView()
}