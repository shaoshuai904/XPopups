package com.maple.demo.ui

import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.maple.demo.R
import com.maple.popups.lib.MsFullScreenPopup
import com.maple.popups.lib.MsPopups
import com.maple.popups.utils.DensityUtils
import com.maple.popups.weight.MsFrameLayout

/**
 * 全屏
 *
 * @author : shaoshuai
 * @date ：2021/2/1
 */
class FullScreenFragment : BaseDemoFragment() {

    override fun initView() {
        super.initView()
    }

    override fun showPopup(view: View) {
        val frameLayout = MsFrameLayout(mContext)
        frameLayout.background = ColorDrawable(ContextCompat.getColor(mContext, R.color.FFaa))
        frameLayout.setRadius(DensityUtils.dp2px(mContext, 12f))
        val padding = DensityUtils.dp2px(mContext, 20f)
        frameLayout.setPadding(padding, padding, padding, padding)
        frameLayout.addView(TextView(mContext).apply {
            setLineSpacing(DensityUtils.dp2px(mContext, 4f).toFloat(), 1.0f)
            setPadding(padding, padding, padding, padding)
            text = "这是自定义显示的内容"
            setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark))
            gravity = Gravity.CENTER
        }, FrameLayout.LayoutParams(DensityUtils.dp2px(mContext, 200f), DensityUtils.dp2px(mContext, 200f)))
        MsPopups.fullScreenPopup(mContext)
                .addView(frameLayout)
                .closeBtn(true)
                .onBlankClick { Toast.makeText(mContext, "点击到空白区域", Toast.LENGTH_SHORT).show() }
                .setDismissListener { Toast.makeText(mContext, "onDismiss", Toast.LENGTH_SHORT).show() }
                .show(view)
    }


    private fun clickMore(v: View) {
        val frameLayout = MsFrameLayout(mContext).apply {
            background = ColorDrawable(ContextCompat.getColor(mContext, R.color.FFaa))
            setRadius(DensityUtils.dp2px(mContext, 12f))
            val padding = DensityUtils.dp2px(mContext, 20f)
            setPadding(padding, padding, padding, padding)
            addView(
                    TextView(mContext).apply {
                        setLineSpacing(DensityUtils.dp2px(mContext, 4f).toFloat(), 1.0f)
                        setPadding(padding, padding, padding, padding)
                        text = "这是自定义显示的内容"
                        gravity = Gravity.CENTER
                    },
                    FrameLayout.LayoutParams(DensityUtils.dp2px(mContext, 200f), DensityUtils.dp2px(mContext, 200f))
            )
        }

        val editParent = MsFrameLayout(mContext).apply {
            minimumHeight = DensityUtils.dp2px(mContext, 48f)
            setRadius(DensityUtils.dp2px(mContext, 24f))
            background = ColorDrawable(ContextCompat.getColor(mContext, R.color.FFaa))
            addView(
                    EditText(mContext).apply {
                        hint = "请输入..."
                        background = null
                        setPadding(DensityUtils.dp2px(mContext, 20f), DensityUtils.dp2px(mContext, 10f), DensityUtils.dp2px(mContext, 20f), DensityUtils.dp2px(mContext, 10f))
                        maxHeight = DensityUtils.dp2px(mContext, 100f)
                    },
                    FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply { gravity = Gravity.CENTER_HORIZONTAL }
            )
        }
        val editFitSystemWindowWrapped = FrameLayout(mContext)
        editFitSystemWindowWrapped.fitsSystemWindows = true
        editFitSystemWindowWrapped.addView(editParent, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        val eLp = ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
            rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            val mar = DensityUtils.dp2px(mContext, 20f)
            leftMargin = mar
            rightMargin = mar
            bottomMargin = mar
        }
        MsPopups.fullScreenPopup(mContext)
                .addView(frameLayout, MsFullScreenPopup.getOffsetHalfKeyboardHeightListener())
                .addView(editFitSystemWindowWrapped, eLp, MsFullScreenPopup.getOffsetKeyboardHeightListener())
                .onBlankClick { popup: MsFullScreenPopup ->
                    popup.dismiss()
                    Toast.makeText(mContext, "点击到空白区域", Toast.LENGTH_SHORT).show()
                }
                .setDismissListener { Toast.makeText(mContext, "onDismiss", Toast.LENGTH_SHORT).show() }
                .show(v)
    }

}