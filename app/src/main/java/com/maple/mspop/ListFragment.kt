package com.maple.mspop

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.maple.mspop.databinding.FragmentQuickActionBinding
import com.maple.popups.lib.MsNormalPopup
import com.maple.popups.lib.MsPopup
import com.maple.popups.lib.MsPopups
import com.maple.popups.utils.DensityUtils

/**
 * 列表功能
 *
 * @author : shaoshuai
 * @date ：2021/2/1
 */
class ListFragment : Fragment() {
    private lateinit var binding: FragmentQuickActionBinding
    private lateinit var mContext: Context
    private var itemCount = 7
    private var showArrow = true // 是否显示箭头
    private var showShadow = true // 是否显示阴影

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_quick_action, container, false)
        binding.lifecycleOwner = this
        initView()
        return binding.root
    }

    private fun initView() {
        binding.fdlShow.setOnClickListener { clickMore(it) }
        // add listener
        with(binding) {
            swArrow.setOnCheckedChangeListener { _, isChecked -> showArrow = isChecked }
            swShadow.setOnCheckedChangeListener { _, isChecked -> showShadow = isChecked }
            sbItemCount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    tvItemCount.text = "item个数: $progress"
                    itemCount = progress
                }
            })
            sbItemCount.progress = 7
        }

    }

    private var mNormalPopup: MsPopup? = null
    private fun clickMore(v: View) {
        val adapter: ArrayAdapter<*> = ArrayAdapter(mContext, R.layout.simple_list_item, getTestData(itemCount))
        val onItemClickListener: AdapterView.OnItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(mContext, "Item " + (i + 1), Toast.LENGTH_SHORT).show()
            mNormalPopup?.dismiss()
        }
        mNormalPopup = MsPopups
                .listPopup(mContext, DensityUtils.dp2px(mContext, 250f), DensityUtils.dp2px(mContext, 300f), adapter, onItemClickListener)
                .setContextBgColor(ContextCompat.getColor(mContext, R.color.FFaa))
                .animStyle(MsNormalPopup.AnimStyle.ANIM_GROW_FROM_CENTER)
                .preferredDirection(MsNormalPopup.Direction.TOP)
                .shadow(true)
                .arrow(false)
//                .offsetYIfTop(dp2px(mContext, 5f))
                .show(v)
    }


    private fun getTestData(count: Int): List<String> {
        val testData = arrayListOf<String>()
        for (i in 0 until count) {
            testData.add("item $i")
        }
        return testData
    }
}