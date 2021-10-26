package com.maple.popups.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maple.popups.databinding.MsItemActionBinding


/**
 * 单选 item适配器
 *
 * @author : shaoshuai
 * @date ：2020/1/7
 */
class QuickActionAdapter(
        private val mContext: Context
) : BaseQuickAdapter<SheetItem, RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding: MsItemActionBinding = MsItemActionBinding.inflate(
            LayoutInflater.from(mContext), parent, false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MyHolder).bind(getItem(position))
    }

    inner class MyHolder(private val binding: MsItemActionBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SheetItem) {
            bindViewClickListener(this)
            binding.apply {
                ivIcon.setImageResource(item.sheetIcon ?: 0)
                tvName.text = item.getShowName()
//                tvName.textSize = config.itemTextSizeSp
//                tvName.setPadding(config.itemPaddingLeft, config.itemPaddingTop, config.itemPaddingRight, config.itemPaddingBottom)
                if (item.isSelected) {
//                    tvName.setTextColor(config.itemTextSelectedColor)
//                    ivMark.setImageDrawable(config.selectMark)
//                    ivMark.visibility = if (config.isShowMark) View.VISIBLE else View.GONE
                } else {
//                    tvName.setTextColor(config.itemTextColor)
//                    ivMark.visibility = View.GONE
                }
            }
        }
    }

}


