package com.maple.demo.ui

import android.graphics.Color
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.maple.demo.R
import com.maple.popups.lib.MsNormalPopup
import com.maple.popups.lib.MsPopup
import com.maple.popups.utils.DensityUtils.dp2px
import com.maple.popups.weight.QMUIWrapContentListView

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
        val mAdapter: ArrayAdapter<*> = ArrayAdapter(mContext, R.layout.simple_list_item, getTestData(mItemCount))
        val mItemClickListener: AdapterView.OnItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            Toast.makeText(mContext, "Item " + (i + 1), Toast.LENGTH_SHORT).show()
            mNormalPopup?.dismiss()
        }

        val listView: ListView = QMUIWrapContentListView(context, mViewHeight.dp2px(mContext))
        listView.adapter = mAdapter
        listView.isVerticalScrollBarEnabled = false
        listView.onItemClickListener = mItemClickListener
        listView.divider = null

        mNormalPopup = MsPopup(context, mViewWidth.dp2px(mContext))
                .setContextView(listView)
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