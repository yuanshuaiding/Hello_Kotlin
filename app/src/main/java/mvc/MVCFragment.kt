package mvc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eric.kotlin.R
import kotlinx.android.synthetic.main.frag_design_mode.*
import mvc.interfaces.IControl
import mvc.interfaces.IModel
import mvc.interfaces.IView

/**
 * mvc模式中的View,负责显示和用户交互,可以在这里完成view关联control,control关联model,model关联view操作
 */
class MVCFragment : Fragment(), IView {
    companion object {

        fun newInstance(): MVCFragment {
            return MVCFragment()
        }
    }
    private var control: IControl? = null

    override fun setControl(control: IControl) {
        this.control = control
    }

    override fun showData(data: CharSequence?) {
        tvResult.setText(data)
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
        //View持有Control的引用
        setControl(MVCControl())
        val model: IModel = MVCModel()
        //Model持有对View的引用
        model.setView(this)
        //Control持有Model的引用
        control?.setModel(model)
        editMy.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // TODO("not implemented")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO("not implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                control?.handleData(s)
            }

        })

        btnClear.setOnClickListener{
            control?.handleData("").apply {
                editMy.setText("")
            }
        }

    }


}