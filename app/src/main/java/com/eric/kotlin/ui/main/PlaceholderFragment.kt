package com.eric.kotlin.ui.main

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.eric.kotlin.R
import com.eric.kotlin.view.VerticalFlipperLayout

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        val textView: TextView = root.findViewById(R.id.section_label)
        pageViewModel.text.observe(this, Observer<String> {
            textView.text = it
        })
        val flipperLayout: VerticalFlipperLayout = root.findViewById(R.id.flipper)
        flipperLayout.setRollingType(VerticalFlipperLayout.BOTTOM_TOP)
        flipperLayout.setAdapter(object : BaseAdapter() {
            override fun getItem(position: Int): Any {
                return position
            }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun getCount(): Int {
                return 5
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val view: View = convertView ?: LayoutInflater.from(context).inflate(
                    R.layout.item_card,
                    parent,
                    false
                )
                view.findViewById<TextView>(R.id.tv_flip).setText("" + position)
                return view
            }

        })
        flipperLayout.startFlipping()
        return root
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}