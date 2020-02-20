package com.zeke.ktx.adapter

import android.graphics.Color
import android.support.v4.view.PagerAdapter
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * author：KingZ
 * date：2020/2/16
 * description：MagicIndicator Demo的Pager适配器
 */
class ExamplePagerAdapter(private var dataList: List<String>?) : PagerAdapter() {
    override fun getCount(): Int {
        return if(dataList == null){
            0
        }else{  dataList!!.size }
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    /**
     * 实例化Item
     */
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val textView = TextView(container.context)
        textView.text = dataList!![position]
        textView.gravity = Gravity.CENTER
        textView.setTextColor(Color.parseColor("#F79817"))
        textView.textSize = 26f
        container.addView(textView)
        return textView
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    override fun getItemPosition(obj: Any): Int {
        val textView = obj as TextView
        val text = textView.text.toString()
        val index = dataList!!.indexOf(text)
        return if (index >= 0) {
            index
        } else PagerAdapter.POSITION_NONE
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return dataList!![position]
    }
}