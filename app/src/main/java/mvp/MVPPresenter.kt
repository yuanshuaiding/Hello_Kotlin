package mvp

import mvp.interfaces.IModel
import mvp.interfaces.IPresenter
import mvp.interfaces.IVeiw

class MVPPresenter:IPresenter {
    private lateinit var model: IModel
    private lateinit var view: IVeiw

    override fun setView(view: IVeiw) {
        this.view=view
    }

    override fun setModel(model: IModel) {
        this.model=model
    }

    override fun onTextChanged(s: CharSequence) {
        view.dataLoading()
        model.handleData(s)
    }

    override fun onClearBtnClicked() {
        view.clearEditTextView()
        model.clearData()
    }

    override fun dataHandled(s: CharSequence) {
        view.showResultView(s)
    }

}