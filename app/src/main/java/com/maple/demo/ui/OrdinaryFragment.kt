package com.maple.demo.ui

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.maple.demo.R
import com.maple.popups.lib.MsNormalPopup
import com.maple.popups.lib.MsPopup
import com.maple.popups.utils.DensityUtils.dp2px

/**
 * 普通Popup
 *
 * @author : shaoshuai
 * @date ：2021/2/1
 */
class OrdinaryFragment : BaseDemoFragment() {

    override fun initView() {
        super.initView()
        setViewWidthVisibility(View.VISIBLE)
        setViewHeightVisibility(View.GONE)
        setItemCountVisibility(View.GONE)
    }

    override fun showPopup(view: View) {
        val textView = TextView(mContext).apply {
            setLineSpacing(dp2px(mContext, 4f).toFloat(), 1.0f)
            val padding = 20f.dp2px(mContext)
            setPadding(padding, padding, padding, padding)
            text = "Popup 可以自定义设置其显示方向、位置和动画。内容宽、高、背景色，边框粗细颜色等"
            setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark))
        }
        MsPopup(mContext, mViewWidth.dp2px(mContext), ViewGroup.LayoutParams.WRAP_CONTENT)
                .setContextView(textView)
                .arrow(mShowArrow)
                .arrowSize(mArrowWidth.dp2px(mContext), mArrowHeight.dp2px(mContext))
                .shadow(mShowShadow)
                .borderWidth(mBorderWidth)
                .borderColor(Color.RED)
                .preferredDirection(showDirection)
                .setAlphaStyle(activity, mAlpha)
                .dimAmount(mDimAmount)
                // .edgeProtection(dp2px(mContext, 40f))
                // .edgeProtection(DensityUtils.dp2px(mContext, 20f), 1000, 1000, 0)
                // .offsetX(QMUIDisplayHelper.dp2px(mContext, 20))
                // .offsetYIfBottom(QMUIDisplayHelper.dp2px(mContext, 5))
                .animStyle(MsNormalPopup.AnimStyle.ANIM_GROW_FROM_CENTER)
                // .setDismissListener { Toast.makeText(mContext, "onDismiss", Toast.LENGTH_SHORT).show() }
                .show(view)

        // 这里只是演示，实际情况应该考虑数据加载完成而 Popup 被 dismiss 的情况
        textView.postDelayed({
            textView.text = "使用 Popup 最好是一开始就确定内容宽高，如果宽高位置会变化，系统会有一个的移动动画不受控制，体验并不好"
        }, 2000)
    }

}