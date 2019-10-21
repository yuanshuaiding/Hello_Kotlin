package mvp

import android.os.Handler
import mvp.interfaces.IModel
import mvp.interfaces.IPresenter

class MVPModel : IModel {
    private lateinit var presenter: IPresenter
    private var handle: Handler = Handler()
    private var result = ""
    override fun setPresenter(presenter: IPresenter) {
        this.presenter = presenter
    }

    override fun handleData(s: CharSequence) {
        handle.removeCallbacksAndMessages(null)
        //模拟处理数据
        result = if (s.isEmpty()) "" else "show data handled by mvp: $s"
        handle.postDelayed({
            presenter.dataHandled(result)

        }, 1000)
    }

    override fun clearData() {
        handle.removeCallbacksAndMessages(null)
        result = ""
        presenter.dataHandled(result)
    }
}