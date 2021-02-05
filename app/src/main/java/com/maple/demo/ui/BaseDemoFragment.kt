package com.maple.demo.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.maple.demo.R
import com.maple.mspop.databinding.FragmentBaseDemoBinding

/**
 * Base Popup
 *
 * @author : shaoshuai
 * @date ：2021/2/1
 */
abstract class BaseDemoFragment : Fragment() {
    lateinit var binding: FragmentBaseDemoBinding
    lateinit var mContext: Context
    var mViewWidth = 250f // 弹窗宽
    var mViewHeight = 250f // 弹窗高
    var mItemCount = 5 // item 个数
    var mBorderWidth = 2 // 边框宽度
    var mDimAmount = 1.0f // 其他区域透明度
    var mShowArrow = true // 是否显示箭头
    var mShowShadow = true // 是否显示阴影

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_base_demo, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    open fun initView() {
        binding.fdlShow.setOnClickListener { showPopup(it) }
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
                            mViewWidth = width
                        }
                        sbViewHeight -> {
                            val height = 100f + 30f * progress
                            tvViewHeight.text = "view最大高度: $height dp"
                            mViewHeight = height
                        }
                        sbItemCount -> {
                            tvItemCount.text = "item 个数: $progress"
                            mItemCount = progress
                        }
                        sbBorderWidth -> {
                            tvBorderWidth.text = "边框宽度: $progress "
                            mBorderWidth = progress
                        }
                        sbDimAmount -> {
                            val dim = 0.1f * progress
                            tvDimAmount.text = "其他区域透明度: $dim "
                            mDimAmount = dim
                        }
                    }
                }
            }
            sbViewWidth.setOnSeekBarChangeListener(seekBarChangeListener)
            sbViewWidth.progress = 5
            sbViewHeight.setOnSeekBarChangeListener(seekBarChangeListener)
            sbViewHeight.progress = 5
            sbItemCount.setOnSeekBarChangeListener(seekBarChangeListener)
            sbItemCount.progress = 5
            sbBorderWidth.setOnSeekBarChangeListener(seekBarChangeListener)
            sbBorderWidth.progress = 2
            sbDimAmount.setOnSeekBarChangeListener(seekBarChangeListener)
            sbDimAmount.progress = 0
            // Switch
            swArrow.setOnCheckedChangeListener { _, isChecked -> mShowArrow = isChecked }
            swShadow.setOnCheckedChangeListener { _, isChecked -> mShowShadow = isChecked }
        }

    }

    abstract fun showPopup(view: View)

    fun setViewWidthVisibility(visibility: Int) {
        binding.tvViewWidth.visibility = visibility
        binding.sbViewWidth.visibility = visibility
    }

    fun setViewHeightVisibility(visibility: Int) {
        binding.tvViewHeight.visibility = visibility
        binding.sbViewHeight.visibility = visibility
    }

    fun setItemCountVisibility(visibility: Int) {
        binding.tvItemCount.visibility = visibility
        binding.sbItemCount.visibility = visibility
    }

    fun seBorderWidthVisibility(visibility: Int) {
        binding.tvBorderWidth.visibility = visibility
        binding.sbBorderWidth.visibility = visibility
    }

    fun seDimAmountVisibility(visibility: Int) {
        binding.tvDimAmount.visibility = visibility
        binding.sbDimAmount.visibility = visibility
    }
}