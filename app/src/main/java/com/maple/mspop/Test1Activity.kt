package com.maple.mspop

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.maple.popups.utils.SheetItem
import com.maple.popups.lib.MsFullScreenPopup
import com.maple.popups.lib.MsNormalPopup
import com.maple.popups.lib.MsPopup
import com.maple.popups.lib.MsPopups
import com.maple.popups.utils.DensityUtils.dp2px
import com.maple.popups.weight.MsFrameLayout

class Test1Activity : FragmentActivity() {
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        mContext = this

        findViewById<View>(R.id.bt_left_top).setOnClickListener { v: View -> clickMore(v) }
        findViewById<View>(R.id.bt_left_bottom).setOnClickListener { v: View -> clickMore1(v) }
        findViewById<View>(R.id.tv_right).setOnClickListener { v: View -> clickMore6(v) }
        findViewById<View>(R.id.bt_left_center).setOnClickListener { v: View -> clickMore3(v) }
        findViewById<View>(R.id.bt_center).setOnClickListener { v: View -> clickMore4(v) }
        findViewById<View>(R.id.bt_right_center).setOnClickListener { v: View -> clickMore5(v) }
        findViewById<View>(R.id.bt_right_bottom).setOnClickListener { v: View -> clickMore2(v) }
    }

    private var mNormalPopup: MsPopup? = null
    private fun clickMore(v: View) {
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
        MsPopups.quickAction(mContext, dp2px(mContext, 56f), dp2px(mContext, 56f))
                .shadow(false)
                .arrow(false)
                .borderWidth(2)
                .borderColor(Color.RED)
                .edgeProtection(100)
//                .edgeProtection(dp2px(mContext, 0f))
                .addActions(datas)
                .setOnItemClickListener { item, _ ->
                    Toast.makeText(mContext, "点击 " + item.sheetName, Toast.LENGTH_SHORT).show()
                }
                .show(v)
    }

    private fun clickMore6(v: View) {
        MsPopups.quickAction(mContext, dp2px(mContext, 56f), dp2px(mContext, 56f))
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

    private fun clickMore5(v: View) {
        val frameLayout = MsFrameLayout(mContext).apply {
            background = ColorDrawable(ContextCompat.getColor(mContext, R.color.FFaa))
            setRadius(dp2px(mContext, 12f))
            val padding = dp2px(mContext, 20f)
            setPadding(padding, padding, padding, padding)
            addView(
                    TextView(mContext).apply {
                        setLineSpacing(dp2px(mContext, 4f).toFloat(), 1.0f)
                        setPadding(padding, padding, padding, padding)
                        text = "这是自定义显示的内容"
                        gravity = Gravity.CENTER
                    },
                    FrameLayout.LayoutParams(dp2px(mContext, 200f), dp2px(mContext, 200f))
            )
        }

        val editParent = MsFrameLayout(mContext).apply {
            minimumHeight = dp2px(mContext, 48f)
            setRadius(dp2px(mContext, 24f))
            background = ColorDrawable(ContextCompat.getColor(mContext, R.color.FFaa))
            addView(
                    EditText(mContext).apply {
                        hint = "请输入..."
                        background = null
                        setPadding(dp2px(mContext, 20f), dp2px(mContext, 10f), dp2px(mContext, 20f), dp2px(mContext, 10f))
                        maxHeight = dp2px(mContext, 100f)
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
            val mar = dp2px(mContext, 20f)
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

    private fun clickMore4(v: View) {
        val frameLayout = MsFrameLayout(mContext)
        frameLayout.background = ColorDrawable(ContextCompat.getColor(mContext, R.color.FFaa))
        frameLayout.setRadius(dp2px(mContext, 12f))
        val padding = dp2px(mContext, 20f)
        frameLayout.setPadding(padding, padding, padding, padding)
        frameLayout.addView(TextView(mContext).apply {
            setLineSpacing(dp2px(mContext, 4f).toFloat(), 1.0f)
            setPadding(padding, padding, padding, padding)
            text = "这是自定义显示的内容"
            setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark))
            gravity = Gravity.CENTER
        }, FrameLayout.LayoutParams(dp2px(mContext, 200f), dp2px(mContext, 200f)))
        MsPopups.fullScreenPopup(mContext)
                .addView(frameLayout)
                .closeBtn(true)
                .onBlankClick { Toast.makeText(mContext, "点击到空白区域", Toast.LENGTH_SHORT).show() }
                .setDismissListener { Toast.makeText(mContext, "onDismiss", Toast.LENGTH_SHORT).show() }
                .show(v)
    }

    private fun clickMore3(v: View) {
        val textView = TextView(mContext)
        textView.setLineSpacing(dp2px(mContext, 4f).toFloat(), 1.0f)
        val padding = dp2px(mContext, 20f)
        textView.setPadding(padding, padding, padding, padding)
        textView.text = "加载中..."
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark))
        mNormalPopup = MsPopups.popup(mContext, dp2px(mContext, 250f))
                .preferredDirection(MsNormalPopup.Direction.BOTTOM)
                .setContextView(textView)
//                .edgeProtection(dp2px(mContext, 40f))
//                .dimAmount(0.6f)
                .shadow(true)
                .animStyle(MsNormalPopup.AnimStyle.ANIM_GROW_FROM_CENTER)
                .setDismissListener { Toast.makeText(mContext, "onDismiss", Toast.LENGTH_SHORT).show() }
                .show(v)

        // 这里只是演示，实际情况应该考虑数据加载完成而 Popup 被 dismiss 的情况
        textView.postDelayed({
            textView.text = "使用 Popup 最好是一开始就确定内容宽高，" +
                    "如果宽高位置会变化，系统会有一个的移动动画不受控制，体验并不好"
        }, 2000)
    }

    private fun clickMore2(v: View) {
        val listItems = arrayListOf(
                "Item 1", "Item 2", "Item 3", "Item 4",
                "Item 5", "Item 6", "Item 7", "Item 8"
        )
        val adapter: ArrayAdapter<*> = ArrayAdapter(mContext, R.layout.simple_list_item, listItems)
        val onItemClickListener: AdapterView.OnItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(mContext, "Item " + (i + 1), Toast.LENGTH_SHORT).show()
            if (mNormalPopup != null) {
                mNormalPopup!!.dismiss()
            }
        }
        mNormalPopup = MsPopups
                .listPopup(mContext, dp2px(mContext, 250f), dp2px(mContext, 300f), adapter, onItemClickListener)
                .setContextBgColor(ContextCompat.getColor(mContext, R.color.FFaa))
                .animStyle(MsNormalPopup.AnimStyle.ANIM_GROW_FROM_CENTER)
                .preferredDirection(MsNormalPopup.Direction.TOP)
                .shadow(true)
                .arrow(false)
//                .offsetYIfTop(dp2px(mContext, 5f))
                .show(v)
    }

    private fun clickMore1(v: View) {
        val textView = TextView(mContext).apply {
            setLineSpacing(dp2px(mContext, 4f).toFloat(), 1.0f)
            val padding = dp2px(mContext, 20f)
            setPadding(padding, padding, padding, padding)
            text = "QMUIBasePopup 可以设置其位置以及显示和隐藏的动画"
            setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark))
        }
        mNormalPopup = MsPopups.popup(mContext, dp2px(mContext, 250f))
                .preferredDirection(MsNormalPopup.Direction.BOTTOM)
                .setContextView(textView)
                .edgeProtection(dp2px(mContext, 20f), 1000, 1000, 0)
                .dimAmount(0.6f)
                .animStyle(MsNormalPopup.AnimStyle.ANIM_GROW_FROM_CENTER)
                //                .offsetX(QMUIDisplayHelper.dp2px(mContext, 20))
                //                .offsetYIfBottom(QMUIDisplayHelper.dp2px(mContext, 5))
                //                .shadow(true)
                //                .arrow(true)
                //                .setDismissListener(new PopupWindow.OnDismissListener() {
                //                    @Override
                //                    public void onDismiss() {
                //                        Toast.makeText(mContext, "onDismiss", Toast.LENGTH_SHORT).show();
                //                    }
                //                })
                .show(v)
    }
}