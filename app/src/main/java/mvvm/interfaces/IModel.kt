package mvvm.interfaces

interface IModel {
    fun setViewModel(vm:IViewModel)
    fun handleData(s:CharSequence)
    fun clearData()
}