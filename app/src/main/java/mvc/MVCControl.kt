package mvc

import android.os.Handler
import mvc.interfaces.IControl
import mvc.interfaces.IModel

class MVCControl : IControl {
    private var modle: IModel? = null
    private var handle: Handler = Handler()
    override fun setModel(model: IModel) {
        this.modle = model
    }

    override fun handleData(s: CharSequence?) {
        handle.removeCallbacksAndMessages(null)
        //模拟处理数据
        val result = if (s.isNullOrEmpty()) "" else "show data handled by control for mvc: $s"
        handle.postDelayed({
            modle?.setData(result)
        }, 1000)
    }
}
