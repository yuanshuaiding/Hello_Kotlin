package mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eric.kotlin.R
import kotlinx.android.synthetic.main.frag_design_mode.*
import mvp.interfaces.IPresenter
import mvp.interfaces.IVeiw

class MVPFragment : Fragment(), IVeiw {
    companion object {
        fun newInstance(): MVPFragment {
            return MVPFragment()
        }
    }

    private lateinit var presenter: IPresenter

    override fun setPresenter(presenter: IPresenter) {
        this.presenter = presenter
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_design_mode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //view关联presenter
        if (!this::presenter.isInitialized) {
            setPresenter(MVPPresenter())
        }
        //model关联presenter
        val model = MVPModel()
        model.setPresenter(presenter)
        //presenter关联view
        presenter.setView(this)
        //presnter关联model
        presenter.setModel(model)

        editMy.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {// TODO("not implemented")
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {// TODO("not implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                presenter.onTextChanged(s.toString())
            }

        })

        btnClear.setOnClickListener {
            presenter.onClearBtnClicked()
        }

    }

    override fun dataLoading() {
        tvResult.setText("data is loading")
    }

    override fun showResultView(s: CharSequence) {
        tvResult.setText(s)
    }

    override fun clearEditTextView() {
        editMy.setText("")
    }
}