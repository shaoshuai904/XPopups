package com.maple.popups.utils

import android.graphics.Color
import java.io.Serializable

/**
 * Sheet Item Bean
 *
 * @author : shaoshuai
 * @date ：2020/5/6
 */
open class SheetItem(
        var sheetId: String,
        var sheetName: String,
        var isSelected: Boolean = false// 是否为选中状态
) : Serializable {
    var sheetIcon: Int? = null
    var textColor: Int = Color.parseColor("#333333")
    var itemClickListener: OnSheetItemClickListener? = null

    constructor(name: String) : this(name, name, false)

    constructor(
            icon: Int,
            name: String
    ) : this(name, name, false) {
        this.sheetIcon = icon
    }

    open fun getShowName() = sheetName

}