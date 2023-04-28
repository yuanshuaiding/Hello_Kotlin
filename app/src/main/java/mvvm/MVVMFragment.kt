package mvvm

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eric.kotlin.R
import kotlinx.android.synthetic.main.frag_design_mode.*

class MVVMFragment : Fragment() {

    companion object {
        fun newInstance(): MVVMFragment {
            return MVVMFragment()
        }
    }

    private lateinit var viewModel: MVVMViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //使用工具类创建ViewModel实例
        viewModel = ViewModelProviders.of(this).get(MVVMViewModel::class.java)
        var model = MVVMModel()
        //viewmodel关联model
        viewModel.setModel(model)
        //model关联viewmodel
        model.setViewModel(viewModel)
        //添加观察者,监测LiveData数据变化
        viewModel.resultLiveData.observe(this, Observer {
            tvResult.setText(it.toString())
            if (TextUtils.isEmpty(it)) {
                editMy.setText("")
            }
        })
        viewModel.loadingLiveData.observe(this, Observer {
            tvResult.setText(it.toString())
        })
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
        editMy.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!TextUtils.isEmpty(s)) {
                    viewModel.onTextChange(s.toString())
                }
            }

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

        })
        btnClear.setOnClickListener {
            viewModel.clearData()
        }
    }
}