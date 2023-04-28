package com.eric.kotlin.view

import android.content.Context
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ListAdapter
import android.widget.ViewFlipper
import com.eric.kotlin.R


/**
 * 支持上下轮播效果的容器控件
 */
class VerticalFlipperLayout : ViewFlipper {
    companion object {
        val TOP_BOTTOM = 0
        val BOTTOM_TOP = 1
    }

    //滚动方向  上下  下上
    private var rollingType = 0
    //是否重复滚动  否,只将所有滚动完毕停止
    //    private boolean mRepeat;
    //滚动间隔 默认1000
    private var mRollingInterval: Int = 3000
    //动画时间
    private var mAnimDuration: Int = 500
    private var mListAdapter: ListAdapter? = null

    constructor(ctx: Context) : this(ctx, null)

    constructor(ctx: Context, attrs: AttributeSet?) : this(ctx, attrs, 0)

    constructor(ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(ctx, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerticalFlipperLayout)
        rollingType = typedArray.getInt(R.styleable.VerticalFlipperLayout_flip_type, 0)
        //        mRepeat = typedArray.getBoolean(R.styleable.VerticalRollingLayout_repeat, false);
        mRollingInterval =
            typedArray.getInt(R.styleable.VerticalFlipperLayout_interval, 3500)
        mAnimDuration = typedArray.getInt(R.styleable.VerticalFlipperLayout_duration, 1000)
        typedArray.recycle()
    }

    init {
        InitRollingAnim()
    }


    fun getRollingType(): Int {
        return rollingType
    }

    /**
     * @param rollingType [.TOP_BOTTOM] [.BOTTOM_TOP]
     */
    fun setRollingType(rollingType: Int) {
        this.rollingType = rollingType
    }

    fun setAdapter(listAdapter: ListAdapter) {
        this.mListAdapter = listAdapter
        initChildView()
    }

    fun notifyDataSetChanged() {
        initChildView()
    }

    private fun initChildView() {
        if (null == mListAdapter) {
            return
        }
        this.removeAllViews()
        for (i in 0 until mListAdapter!!.getCount()) {
            val itemView = mListAdapter!!.getView(i, null, this)
            this.addView(itemView)
        }
    }

    /**
     * 初始化动画
     */
    private fun InitRollingAnim() {
        this.clearAnimation()
        var rollingAnimIn: TranslateAnimation?
        var rollingAnimOut: TranslateAnimation?
        when (rollingType) {
            BOTTOM_TOP//从下到上
            -> {
                rollingAnimOut = getRollingAnim(0f, 1f)
                rollingAnimIn = getRollingAnim(-1f, 0f)
            }
            TOP_BOTTOM//从上到下
            -> {
                rollingAnimIn = getRollingAnim(1f, 0f)
                rollingAnimOut = getRollingAnim(0f, -1f)
            }
            else -> {
                rollingAnimIn = getRollingAnim(1f, 0f)
                rollingAnimOut = getRollingAnim(0f, -1f)
            }
        }
        this.setFlipInterval(mRollingInterval)
        this.inAnimation = rollingAnimIn
        this.outAnimation = rollingAnimOut
    }

    /**
     * 动画
     *
     * @param fromY
     * @param toY
     * @return
     */
    //    //int fromXType, float fromXValue, int toXType, float toXValue,int fromYType, float fromYValue, int toYType, float toYValue
    private fun getRollingAnim(fromY: Float, toY: Float): TranslateAnimation {
        val translateAnimation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            fromY,
            Animation.RELATIVE_TO_SELF,
            toY
        )
        translateAnimation.duration = mAnimDuration.toLong()
        return translateAnimation
    }
}