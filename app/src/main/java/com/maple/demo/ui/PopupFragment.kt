package com.maple.demo.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.maple.demo.R
import com.maple.demo.databinding.FragmentBasePopupBinding
import com.maple.demo.base.BasePopupWindow.AZIMUTH
import com.maple.demo.base.MorePopWindow

/**
 * 列表功能
 *
 * @author : shaoshuai
 * @date ：2021/2/1
 */
class PopupFragment : Fragment() {
    private lateinit var binding: FragmentBasePopupBinding
    private lateinit var mContext: Context
    var azimuth = AZIMUTH.Bottom

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_base_popup, container, false)
        binding.lifecycleOwner = this
        initView()
        return binding.root
    }

    private fun initView() {
        binding.fdlShow.setOnClickListener { clickMore(it) }
        // add listener
        with(binding) {
            rgShowLocation.setOnCheckedChangeListener { group: RadioGroup?, checkedId: Int ->
                azimuth = when (checkedId) {
                    R.id.rb_up -> AZIMUTH.Top
                    R.id.rb_down -> AZIMUTH.Bottom
                    R.id.rb_left -> AZIMUTH.Left
                    R.id.rb_right -> AZIMUTH.Right
                    else -> AZIMUTH.Bottom
                }
            }
        }

    }

    private fun clickMore(v: View) {
        MorePopWindow(mContext)
                .setOnItemClickListener { v1: View ->
                    when (v1.id) {
                        R.id.tvClearPagePen -> Toast.makeText(mContext, "clear pen", Toast.LENGTH_SHORT).show()
                        R.id.tvClearPagePenAndContents -> Toast.makeText(mContext, "clear page", Toast.LENGTH_SHORT).show()
                        R.id.tvClearPageRecords -> Toast.makeText(mContext, "clear page all", Toast.LENGTH_SHORT).show()
                        R.id.tvDelPage -> Toast.makeText(mContext, "del page", Toast.LENGTH_SHORT).show()
                    }
                }
                .setAlphaStyle(v, 0.6f)
                .showPopupWindow(v, azimuth, 2f)
    }


}