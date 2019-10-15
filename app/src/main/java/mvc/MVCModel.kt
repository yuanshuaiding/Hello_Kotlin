package mvc

import mvc.interfaces.IModel
import mvc.interfaces.IView

class MVCModel : IModel {
    private var view: IView? = null

    override fun setView(view: IView) {
        this.view = view
    }

    override fun setData(data: CharSequence?) {
        view?.showData(data)
    }
}