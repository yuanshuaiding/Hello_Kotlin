package mvvm

import android.os.Handler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mvvm.interfaces.IModel
import mvvm.interfaces.IViewModel

class MVVMModel : IModel {
    private var result: String = ""
    private lateinit var viewModel: IViewModel
    private val handle: Handler by lazy { Handler() }

    override fun setViewModel(vm: IViewModel) {
        this.viewModel = vm
    }

    override fun handleData(s: CharSequence) {
        //模拟处理数据
        result = if (s.isEmpty()) "" else "show data handled by mvvm: $s"
        //模拟加载进度
        GlobalScope.launch {
            var percent = 0
            repeat(100){
                delay(10)
                percent += 1
                viewModel.onDataLoading(percent)
            }

        }
        handle.postDelayed({
            viewModel.dataHandled(result)
        }, 1500)
    }

    override fun clearData() {
        result = ""
        viewModel.dataCleared()
    }


}