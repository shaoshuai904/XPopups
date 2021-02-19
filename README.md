# XPopups - 万能样式Popup

XPopup中内置多种常见样式的PopupWindow。支持链式调用。

```
    MsBasePopup
        └ MsNormalPopup
            └ MsPopup
            └ MsQuickActionPopup
```

基础自定义设置

 - 自定义`窗体宽高`、`内容View`、`四周圆角弧度`
 - `边框`粗细、颜色、是否显示
 - `箭头`大小（宽、高）、显示位置、是否显示
 - `阴影`颜色、阴影高度和透明度、是否显示
 - `显示方向`（上、下、左、右、居中），显示时，指定区域 `透明度`及`黑暗度`。
 - `四周边缘保护间距`、与目标View `距离边距`
 - `显示隐藏动画`

MsQuickActionPopup自定义设置

 - Action `Item宽高`
 - 是否显示左滑右滑箭头、箭头宽度

### 快速使用

**Step 1.** Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
```

**Step 2.** Add the dependency

```groovy
dependencies {
	implementation 'com.github.shaoshuai904:XPopups:1.0.0'
}
```


![show_01](/screens/show_01.png)


###  MsPopup

	正常的PopupWindow: [ 内容View + 边框 + 阴影 + 圆角弧度 + 指示箭头 + 显示方向 + 动画 + 各种边距 ]

```java
        // 自定义视图，显示啥内容 完全由你自定义
        val textView = TextView(mContext).apply {
            setLineSpacing(dp2px(mContext, 4f).toFloat(), 1.0f)
            val padding = 20f.dp2px(mContext)
            setPadding(padding, padding, padding, padding)
            text = "Popup 可以自定义设置其显示方向、位置和动画。内容宽、高、背景色，边框粗细颜色等"
            setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark))
        }
        MsPopup(mContext, mViewWidth.dp2px(mContext), ViewGroup.LayoutParams.WRAP_CONTENT)
                .setContextView(textView)
                .arrow(mShowArrow)
                .arrowSize(mArrowWidth.dp2px(mContext), mArrowHeight.dp2px(mContext))
                .shadow(mShowShadow)
                .borderWidth(mBorderWidth)
                .borderColor(Color.RED)
                .preferredDirection(showDirection)
                .setAlphaStyle(activity, mAlpha)
                .dimAmount(mDimAmount)
                // .edgeProtection(dp2px(mContext, 40f))
                // .edgeProtection(DensityUtils.dp2px(mContext, 20f), 1000, 1000, 0)
                // .offsetX(QMUIDisplayHelper.dp2px(mContext, 20))
                // .offsetYIfBottom(QMUIDisplayHelper.dp2px(mContext, 5))
                .animStyle(MsNormalPopup.AnimStyle.ANIM_GROW_FROM_CENTER)
                // .setDismissListener { Toast.makeText(mContext, "onDismiss", Toast.LENGTH_SHORT).show() }
                .show(view)
```

### MsQuickActionPopup

	快捷功能PopupWindow，在正常Popup的基础上，新增: [ Item大小 + 左滑右滑箭头 ]

```java
        MsQuickActionPopup(mContext, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .actionWidth(56f.dp2px(mContext))
                .actionHeight(56f.dp2px(mContext))
                .arrow(mShowArrow)
                .arrowSize(mArrowWidth.dp2px(mContext), mArrowHeight.dp2px(mContext))
                .shadow(mShowShadow)
                .borderWidth(mBorderWidth)
                .borderColor(Color.RED)
                .preferredDirection(showDirection)
                .setAlphaStyle(view, mAlpha)
                .dimAmount(mDimAmount)
                // .dimAmount(mDimAmount)
                // .shadowElevation(21, 0.9f)
                // .arrowSize()
                // .edgeProtection(100)
                .addActions(getTestData(mItemCount))
                // .addAction(SheetItem(android.R.drawable.ic_menu_share, "分享"))
                .setOnItemClickListener { item, _ ->
                    Toast.makeText(mContext, "点击 " + item.sheetName, Toast.LENGTH_SHORT).show()
                }
                .show(view)
```




