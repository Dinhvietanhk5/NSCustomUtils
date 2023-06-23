//package com.newsoft.nsextension
//
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import androidx.fragment.app.FragmentPagerAdapter
//
//class PaperAdapter(fm: FragmentManager?) : FragmentPagerAdapter(
//    fm!!
//) {
//    private val listTab: MutableList<Fragment>
//    private val titleTab: MutableList<String>
//
//    init {
//        listTab = ArrayList()
//        titleTab = ArrayList()
//    }
//
//    fun addTab(fragment: Fragment, title: String) {
//        listTab.add(fragment)
//        titleTab.add(title)
//    }
//
//    fun addTab(fragment: Fragment) {
//        listTab.add(fragment)
//        titleTab.add("")
//    }
//
//    fun setTitleTab(pos: Int, title: String) {
//        titleTab[pos] = title
//    }
//
//    fun clear() {
//        listTab.clear()
//    }
//
//    override fun getPageTitle(position: Int): CharSequence? {
//        return titleTab[position]
//    }
//
//    override fun getItem(position: Int): Fragment {
//        return listTab[position]
//    }
//
//    override fun getCount(): Int {
//        return listTab.size
//    }
//}
