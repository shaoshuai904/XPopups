package com.maple.demo.ui

import android.graphics.Color
import android.view.View
import android.widget.Toast
import com.maple.popups.lib.MsPopups
import com.maple.popups.utils.DensityUtils.dp2px
import com.maple.popups.utils.SheetItem

/**
 * 快捷功能
 *
 * @author : shaoshuai
 * @date ：2021/2/1
 */
class QuickActionFragment : BaseDemoFragment() {

    override fun initView() {
        super.initView()
        setViewWidthVisibility(View.GONE)
        setViewHeightVisibility(View.GONE)
    }

    override fun showPopup(view: View) {
        MsPopups.quickAction(mContext, 56f.dp2px(mContext), 56f.dp2px(mContext))
                .arrow(mShowArrow)
                .shadow(mShowShadow)
                .borderWidth(mBorderWidth)
                .borderColor(Color.RED)
                .dimAmount(mDimAmount)
                // .shadowElevation(21, 0.9f)
                // .arrowSize()
                // .edgeProtection(100)
                .addActions(getTestData(mItemCount))
                // .addAction(SheetItem(android.R.drawable.ic_menu_share, "分享"))
                .setOnItemClickListener { item, _ ->
                    Toast.makeText(mContext, "点击 " + item.sheetName, Toast.LENGTH_SHORT).show()
                }
                .show(view)
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