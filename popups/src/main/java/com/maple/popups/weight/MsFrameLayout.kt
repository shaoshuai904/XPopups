package com.maple.popups.weight

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import com.maple.popups.lib.MyViewUtils

/**
 * 圆角 + 阴影 + 限定最大最小宽高
 *
 * @author : shaoshuai
 * @date ：2020/12/15
 */
open class MsFrameLayout : FrameLayout {
    // size
    private val mMaxWidth = 0
    private val mMaxHeight = 0
    private val mMinWidth = 0
    private val mMinHeight = 0
    // round
    private var mRadius = 0f
    private var mBorderWidth = 0f
    @ColorInt
    private var mBorderColor = 0
    // shadow
    private var mShadowElevation = 0f
    private var mShadowAlpha = 0f
    @ColorInt
    private var mShadowColor = Color.BLACK

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {}


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val mWidthSpec = if (mMaxWidth > 0 && MeasureSpec.getSize(widthMeasureSpec) > mMaxWidth) {
            val mode = if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) MeasureSpec.AT_MOST else MeasureSpec.EXACTLY
            MeasureSpec.makeMeasureSpec(mMaxWidth, mode)
        } else widthMeasureSpec
        val mHeightSpec = if (mMaxHeight > 0 && MeasureSpec.getSize(heightMeasureSpec) > mMaxHeight) {
            val mode = if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) MeasureSpec.AT_MOST else MeasureSpec.EXACTLY
            MeasureSpec.makeMeasureSpec(mMaxHeight, mode)
        } else heightMeasureSpec

        super.onMeasure(mWidthSpec, mHeightSpec)

        val minW = if (MeasureSpec.getMode(mWidthSpec) != MeasureSpec.EXACTLY && measuredWidth < mMinWidth) {
            MeasureSpec.makeMeasureSpec(mMinWidth, MeasureSpec.EXACTLY)
        } else mWidthSpec
        val minH = if (MeasureSpec.getMode(mHeightSpec) != MeasureSpec.EXACTLY && measuredHeight < mMinHeight) {
            MeasureSpec.makeMeasureSpec(mMinHeight, MeasureSpec.EXACTLY)
        } else mHeightSpec

        if (mWidthSpec != minW || mHeightSpec != minH) {
            super.onMeasure(minW, minH)
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        MyViewUtils.dispatchRoundBorderDraw(this, canvas, mRadius, mBorderWidth, mBorderColor)
    }

    fun setRadius(radius: Int) {
        setRadiusAndShadow(radius.toFloat(), mShadowElevation, mShadowAlpha)
    }

    fun setRadiusAndShadow(radius: Float, shadowElevation: Float, shadowAlpha: Float) {
        mRadius = radius
        mShadowElevation = shadowElevation
        mShadowAlpha = shadowAlpha
        // mShadowColor = Color.BLACK
        MyViewUtils.setRadiusAndShadow(this, mRadius, mShadowElevation, mShadowAlpha, mShadowColor)
    }

    fun setBorderColor(@ColorInt borderColor: Int) {
        mBorderColor = borderColor
        invalidate()
    }

    fun setBorderWidth(borderWidth: Float) {
        mBorderWidth = borderWidth
        invalidate()
    }
}