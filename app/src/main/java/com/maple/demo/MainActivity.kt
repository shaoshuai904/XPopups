package com.maple.demo

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.maple.demo.databinding.ActivityMainBinding
import com.maple.demo.ui.ListFragment
import com.maple.demo.ui.OrdinaryFragment
import com.maple.demo.ui.QuickActionFragment

/**
 * 示例Demo
 *
 * @author : shaoshuai
 * @date ：2021/2/1
 */
class MainActivity : FragmentActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        val map = mapOf(
                Pair("普通弹窗", OrdinaryFragment()),
                Pair("快捷功能", QuickActionFragment()),
                Pair("列表", ListFragment())
        )
        // view pager
        binding.vpPager.adapter = MyFragmentPagerAdapter(supportFragmentManager, map)
        binding.tlTab.setupWithViewPager(binding.vpPager)
    }

    class MyFragmentPagerAdapter(
            fm: FragmentManager,
            var fragments: Map<String, Fragment>
    ) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getPageTitle(position: Int) = fragments.keys.elementAt(position)
        override fun getItem(i: Int) = fragments.values.elementAt(i)
        override fun getCount() = fragments.size
    }
}