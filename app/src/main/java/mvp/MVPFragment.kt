package mvp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eric.kotlin.R
import kotlinx.android.synthetic.main.frag_design_mode.*
import mvp.interfaces.IPresenter
import mvp.interfaces.IVeiw

/**
 * mvp模式中的view,可以在这里完成view关联presenter,presenter关联view,presenter关联model,model关联presenter.
 * 注意,view和model没有关联,全部通过presenter中转
 */
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
        //presenter关联view
        presenter.setView(this)

        //model关联presenter
        val model = MVPModel()
        model.setPresenter(presenter)
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