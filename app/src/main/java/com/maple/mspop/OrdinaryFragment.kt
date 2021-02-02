package com.maple.mspop

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.maple.popups.lib.MsNormalPopup
import com.maple.popups.lib.MsPopups
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
            text = "QMUIBasePopup 可以设置其位置以及显示和隐藏的动画"
            setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark))
        }
        MsPopups.popup(mContext, mViewWidth.dp2px(mContext))
                .preferredDirection(MsNormalPopup.Direction.BOTTOM)
                .setContextView(textView)
                .dimAmount(mDimAmount)
                .arrow(mShowArrow)
                .shadow(mShowShadow)
                .borderWidth(mBorderWidth)
                .borderColor(Color.RED)
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