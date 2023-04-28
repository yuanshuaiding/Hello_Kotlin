package mvvm.interfaces

interface IViewModel {
    fun setModel(model: IModel)
    fun onTextChange(s: CharSequence)
    fun dataHandled(s: CharSequence)
    fun dataCleared()
    fun onDataLoading(percent: Int)
    fun clearData()
}
