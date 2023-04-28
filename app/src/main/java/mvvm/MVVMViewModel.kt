package mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mvvm.interfaces.IModel
import mvvm.interfaces.IViewModel

class MVVMViewModel : IViewModel, ViewModel() {
    private lateinit var model: IModel
    //可以被观察的livedata,用于通知view显示结果
    public val resultLiveData = MutableLiveData<CharSequence>()
    //可以被观察的livedata,用于通知view显示进度
    public val loadingLiveData = MutableLiveData<Int>()

    override fun setModel(model: IModel) {
        this.model = model
    }

    override fun onTextChange(s: CharSequence) {
        model.handleData(s)
    }

    override fun dataHandled(s: CharSequence) {
        resultLiveData.value = s
    }

    override fun clearData() {
        model.clearData()
    }

    override fun dataCleared() {
        resultLiveData.postValue("")
    }

    override fun onDataLoading(percent: Int) {
        loadingLiveData.postValue(percent)
    }
}