package com.maple.mspop

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.maple.mspop.databinding.FragmentQuickActionBinding
import com.maple.popups.lib.MsPopups
import com.maple.popups.utils.DensityUtils
import com.maple.popups.utils.SheetItem

/**
 * 快捷功能
 *
 * @author : shaoshuai
 * @date ：2021/2/1
 */
class QuickActionFragment : Fragment() {
    private lateinit var binding: FragmentQuickActionBinding
    private lateinit var mContext: Context
    private var itemCount = 3
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
        binding.fdlShow.setOnClickListener { clickQuickAction(it) }
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
            sbItemCount.progress = 3
        }

    }

    private fun clickQuickAction(v: View) {
        MsPopups.quickAction(mContext, DensityUtils.dp2px(mContext, 56f), DensityUtils.dp2px(mContext, 56f))
                .shadow(showShadow)
                // .shadowElevation(21, 0.9f)
                .arrow(showArrow)
                //.arrowSize()
                // .borderWidth(2)
                // .borderColor(Color.RED)
                // .edgeProtection(100)
                .addActions(getTestData(itemCount))
                .setOnItemClickListener { item, _ ->
                    Toast.makeText(mContext, "点击 " + item.sheetName, Toast.LENGTH_SHORT).show()
                }
                .show(v)
    }

    private fun clickMore(v: View) {
        MsPopups.quickAction(mContext, DensityUtils.dp2px(mContext, 56f), DensityUtils.dp2px(mContext, 56f))
                .shadow(true)
                .borderColor(Color.TRANSPARENT)
//                .edgeProtection(dp2px(mContext, 20f))
                .addAction(SheetItem(android.R.drawable.ic_menu_add, "添加"))
                .addAction(SheetItem(android.R.drawable.ic_menu_crop, "截图"))
                .addAction(SheetItem(android.R.drawable.ic_menu_share, "分享"))
                .setOnItemClickListener { item, _ ->
                    Toast.makeText(mContext, "点击 " + item.sheetName, Toast.LENGTH_SHORT).show()
                }
                .show(v)
    }


    private fun getTestData(count: Int): List<SheetItem> {
        val datas = arrayListOf(
                SheetItem(android.R.drawable.ic_menu_add, "添加"),
                SheetItem(android.R.drawable.ic_menu_delete, "删除"),
                SheetItem(android.R.drawable.ic_menu_call, "电话"),
                SheetItem(android.R.drawable.ic_menu_help, "帮助"),
                SheetItem(android.R.drawable.ic_menu_mylocation, "我的位置"),
                SheetItem(android.R.drawable.ic_menu_camera, "相册"),
                SheetItem(android.R.drawable.ic_menu_share, "分享"),
                SheetItem(android.R.drawable.ic_menu_crop, "截图")
        )
        val testData = arrayListOf<SheetItem>()
        for (i in 0 until count) {
            testData.add(datas.random())
        }
        return testData
    }
}