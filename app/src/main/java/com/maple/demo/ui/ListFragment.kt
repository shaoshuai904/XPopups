package com.maple.demo.ui

import android.graphics.Color
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.maple.demo.R
import com.maple.popups.lib.MsNormalPopup
import com.maple.popups.lib.MsPopup
import com.maple.popups.lib.MsPopups
import com.maple.popups.utils.DensityUtils.dp2px

/**
 * 列表功能
 *
 * @author : shaoshuai
 * @date ：2021/2/1
 */
class ListFragment : BaseDemoFragment() {

    override fun initView() {
        super.initView()
        binding.nsViewWidth.currentValue = 100f
        binding.nsViewHeight.currentValue = 160f
    }

    private var mNormalPopup: MsPopup? = null
    override fun showPopup(view: View) {
        val adapter: ArrayAdapter<*> = ArrayAdapter(mContext, R.layout.simple_list_item, getTestData(mItemCount))
        val onItemClickListener: AdapterView.OnItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(mContext, "Item " + (i + 1), Toast.LENGTH_SHORT).show()
            mNormalPopup?.dismiss()
        }
        mNormalPopup = MsPopups
                .listPopup(mContext, mViewWidth.dp2px(mContext), mViewHeight.dp2px(mContext), adapter, onItemClickListener)
                .setContextBgColor(ContextCompat.getColor(mContext, R.color.FFaa))
                .arrow(mShowArrow)
                .arrowSize(mArrowWidth.dp2px(mContext), mArrowHeight.dp2px(mContext))
                .shadow(mShowShadow)
                .borderWidth(mBorderWidth)
                .borderColor(Color.BLACK)
                .preferredDirection(showDirection)
                .setAlphaStyle(activity, mAlpha)
                .dimAmount(mDimAmount)
                // .offsetYIfTop(dp2px(mContext, 5f))
                .animStyle(MsNormalPopup.AnimStyle.ANIM_GROW_FROM_CENTER)
                .show(view)
    }


    private fun getTestData(count: Int): List<String> {
        val testData = arrayListOf<String>()
        for (i in 0 until count) {
            testData.add("item $i")
        }
        return testData
    }
}