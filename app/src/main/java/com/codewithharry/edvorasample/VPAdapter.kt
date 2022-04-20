package com.codewithharry.edvorasample

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class VPAdapter(fm: FragmentManager, behaviour: Int): FragmentPagerAdapter(fm,behaviour) {

    private val fragmentArrayList: ArrayList<Fragment> = arrayListOf<Fragment>()
    private val fragmentTitle: ArrayList<String> = arrayListOf<String>()

    override fun getCount(): Int {
        return fragmentArrayList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentArrayList.get(position)
    }

    public fun addFragment(fragment: Fragment,title: String) {
        fragmentArrayList.add(fragment)
        fragmentTitle.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitle.get(position)
    }
}