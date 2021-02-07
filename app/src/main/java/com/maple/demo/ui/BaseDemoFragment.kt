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
import com.maple.demo.databinding.FragmentBaseDemoBinding
import com.maple.demo.view.NumberStepper
import com.maple.popups.lib.MsNormalPopup

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
    var showDirection = MsNormalPopup.Direction.BOTTOM
    var mShowArrow = true // 是否显示箭头
    var mArrowWidth = 8f // 箭头宽
    var mArrowHeight = 0f // 箭头高
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
            val numberValueChangeListener = object : NumberStepper.NumberStepperValueChangeListener {
                override fun onValueChange(view: View, value: Float) {
                    when (view) {
                        nsViewWidth -> mViewWidth = value
                        nsViewHeight -> mViewHeight = value
                        nsArrowWidth -> mArrowWidth = value
                        nsArrowHeight -> mArrowHeight = value
                    }
                }
            }
            nsViewWidth.setOnValueChangeListener(numberValueChangeListener)
            nsViewHeight.setOnValueChangeListener(numberValueChangeListener)
            mViewWidth = nsViewWidth.currentValue
            mViewHeight = nsViewHeight.currentValue
            nsArrowWidth.setOnValueChangeListener(numberValueChangeListener)
            nsArrowHeight.setOnValueChangeListener(numberValueChangeListener)
            mArrowWidth = nsArrowWidth.currentValue
            mArrowHeight = nsArrowHeight.currentValue

            val seekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    when (seekBar) {
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
            sbItemCount.setOnSeekBarChangeListener(seekBarChangeListener)
            sbItemCount.progress = 5
            sbBorderWidth.setOnSeekBarChangeListener(seekBarChangeListener)
            sbBorderWidth.progress = 2
            sbDimAmount.setOnSeekBarChangeListener(seekBarChangeListener)
            sbDimAmount.progress = 0
            //
            rgShowDirection.setOnCheckedChangeListener { _, checkedId ->
                showDirection = when (checkedId) {
                    R.id.rb_top -> MsNormalPopup.Direction.TOP
                    R.id.rb_bottom -> MsNormalPopup.Direction.BOTTOM
                    R.id.rb_left -> MsNormalPopup.Direction.LEFT
                    R.id.rb_right -> MsNormalPopup.Direction.RIGHT
                    R.id.rb_center -> MsNormalPopup.Direction.CENTER_IN_SCREEN
                    else -> MsNormalPopup.Direction.BOTTOM
                }
            }
            rgShowDirection.check(R.id.rb_bottom)
            // Switch
            swArrow.setOnCheckedChangeListener { _, isChecked -> mShowArrow = isChecked }
            swShadow.setOnCheckedChangeListener { _, isChecked -> mShowShadow = isChecked }
        }

    }

    abstract fun showPopup(view: View)

    fun setViewWidthVisibility(visibility: Int) {
        // binding.tvViewWidth.visibility = visibility
        // binding.sbViewWidth.visibility = visibility
        binding.nsViewWidth.visibility = visibility
    }

    fun setViewHeightVisibility(visibility: Int) {
        // binding.tvViewHeight.visibility = visibility
        // binding.sbViewHeight.visibility = visibility
        binding.nsViewHeight.visibility = visibility
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