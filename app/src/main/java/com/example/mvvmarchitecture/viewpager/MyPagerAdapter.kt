package com.example.mvvmarchitecture.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class MyPagerAdapter(var supportManger: FragmentManager) : FragmentPagerAdapter(supportManger,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var item = getItems(false)

    private fun getItems(isEnable: Boolean) =
        mutableListOf<Triple<Int, Fragment, String>>().apply {
            add(Triple(10, Fragment1(), "First"))
            if (isEnable)
                add(Triple(20, Fragment2(), "Second"))
            add(Triple(30, Fragment3(), "Third"))
        }

    override fun getCount() = item.size

    override fun getItem(position: Int): Fragment {
        return item[position].second
    }

    override fun getItemId(position: Int): Long {
        return item[position].first.toLong()
    }


    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    fun refresh(isEnable: Boolean) {
        item = getItems(isEnable = isEnable)
        notifyDataSetChanged()
    }

}