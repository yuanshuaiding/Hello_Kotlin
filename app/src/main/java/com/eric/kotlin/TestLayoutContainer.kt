package com.eric.kotlin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_layoutcontainer.*
import kotlinx.android.synthetic.main.item_layoutcontainer_test.*

class TestLayoutContainer : AppCompatActivity() {
    companion object {
        fun action(activity: AppCompatActivity) {
            activity.startActivity(Intent(activity, TestLayoutContainer::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layoutcontainer)
        tv_layoutcontainer.text=this.localClassName
        recycle_view.layoutManager =
            LinearLayoutManager(this)
        recycle_view.adapter = MyAdapter()
    }
}

class MyAdapter : RecyclerView.Adapter<VH>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VH {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_layoutcontainer_test, p0, false)
        return VH(view)
    }

    override fun getItemCount(): Int {
        return 100
    }

    override fun onBindViewHolder(vh: VH, p: Int) {
        vh.setupTitle("Test Layout $p")
    }

}

class VH(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {

    override val containerView: View?
        get() = itemView

    fun setupTitle(title: String) {
        tv_title.text = title
    }
}
