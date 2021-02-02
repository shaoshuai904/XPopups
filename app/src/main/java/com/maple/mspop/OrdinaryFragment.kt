package com.maple.mspop

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.maple.mspop.databinding.FragmentOrdinaryBinding
import com.maple.popups.lib.MsNormalPopup
import com.maple.popups.lib.MsPopups
import com.maple.popups.utils.DensityUtils.dp2px

/**
 * 普通Popup
 *
 * @author : shaoshuai
 * @date ：2021/2/1
 */
class OrdinaryFragment : Fragment() {
    private lateinit var binding: FragmentOrdinaryBinding
    private lateinit var mContext: Context
    private var viewWidth = 250f // 弹窗宽
    private var borderWidth = 2 // 边框宽度
    private var dimAmount = 1.0f // 其他区域透明度
    private var showArrow = true // 是否显示箭头
    private var showShadow = true // 是否显示阴影

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ordinary, container, false)
        binding.lifecycleOwner = this
        initView()
        return binding.root
    }

    private fun initView() {
        binding.fdlShow.setOnClickListener { clickMore(it) }
        // add listener
        with(binding) {
            val seekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    when (seekBar) {
                        sbViewWidth -> {
                            val width = 100f + 30f * progress
                            tvViewWidth.text = "view宽度: $width dp"
                            viewWidth = width
                        }
                        sbBorderWidth -> {
                            tvBorderWidth.text = "边框宽度: $progress "
                            borderWidth = progress
                        }
                        sbDimAmount -> {
                            val dim = 0.1f * progress
                            tvDimAmount.text = "其他区域透明度: $dim "
                            dimAmount = dim
                        }
                    }
                }
            }
            swArrow.setOnCheckedChangeListener { _, isChecked -> showArrow = isChecked }
            swShadow.setOnCheckedChangeListener { _, isChecked -> showShadow = isChecked }
            sbViewWidth.setOnSeekBarChangeListener(seekBarChangeListener)
            sbViewWidth.progress = 5
            sbDimAmount.setOnSeekBarChangeListener(seekBarChangeListener)
            sbDimAmount.progress = 0
            sbBorderWidth.setOnSeekBarChangeListener(seekBarChangeListener)
            sbBorderWidth.progress = 2
        }

    }

    private fun clickMore(v: View) {
        val textView = TextView(mContext).apply {
            setLineSpacing(dp2px(mContext, 4f).toFloat(), 1.0f)
            val padding = 20f.dp2px(mContext)
            setPadding(padding, padding, padding, padding)
            text = "QMUIBasePopup 可以设置其位置以及显示和隐藏的动画"
            setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark))
        }
        MsPopups.popup(mContext, viewWidth.dp2px(mContext))
                .preferredDirection(MsNormalPopup.Direction.BOTTOM)
                .setContextView(textView)
                .dimAmount(dimAmount)
                .arrow(showArrow)
                .shadow(showShadow)
                .borderWidth(borderWidth)
                .borderColor(Color.RED)
                // .edgeProtection(dp2px(mContext, 40f))
                // .edgeProtection(DensityUtils.dp2px(mContext, 20f), 1000, 1000, 0)
                // .offsetX(QMUIDisplayHelper.dp2px(mContext, 20))
                // .offsetYIfBottom(QMUIDisplayHelper.dp2px(mContext, 5))
                .animStyle(MsNormalPopup.AnimStyle.ANIM_GROW_FROM_CENTER)
                // .setDismissListener { Toast.makeText(mContext, "onDismiss", Toast.LENGTH_SHORT).show() }
                .show(v)

        // 这里只是演示，实际情况应该考虑数据加载完成而 Popup 被 dismiss 的情况
        textView.postDelayed({
            textView.text = "使用 Popup 最好是一开始就确定内容宽高，如果宽高位置会变化，系统会有一个的移动动画不受控制，体验并不好"
        }, 2000)
    }

}