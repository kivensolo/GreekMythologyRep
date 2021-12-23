package com.zeke.demo.widget

import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.wanandroid.fragemnts.CommonFragment
import com.zeke.demo.R
import com.zeke.demo.color.ColorViewModel
import kotlinx.android.synthetic.main.activity_toobar_demo.*

/**
 * author：ZekeWang
 * date：2021/12/5
 * description：ToolBar的使用，以及与ActionBar的区别
 * 一、Actionbar的五大用途：
 *  1.  可以使用图标做导航
2． 提供导航标签
3． 提供下拉列表导航
4． 为菜单添加动作视图
5． 为菜单添加Actionprovide

二、ToolBar详解  https://www.jianshu.com/p/6244687137d2
 */
class ToolBarFragment : CommonFragment<ColorViewModel>() {
    override fun getLayoutResID(): Int = R.layout.activity_toobar_demo

    override val viewModel: ColorViewModel by viewModels {
        ViewModelFactory.build { ColorViewModel() }
    }

    override fun onViewCreated() {
        super.onViewCreated()
        val toolbarView = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbarView?.apply {
            // ActionBar的处理
            (activity as ToolBarTestActivity).setSupportActionBar(this)
            (activity as ToolBarTestActivity).supportActionBar?.apply {
                // 显示标题
                setDisplayShowTitleEnabled(true)
            }
            findViewById<TextView>(R.id.tvTitle)?.setTextColor(resources.getColor(R.color.google_blue))

        }

        homeAsUpEnabledCheckBox.apply {
            setOnCheckedChangeListener { buttonView, isChecked ->
                (activity as ToolBarTestActivity).supportActionBar?.apply {
                    // 给左上角图标加上一个返回的图标 。
                    // 对应ActionBar.DISPLAY_HOME_AS_UP  默认显示HomeAsUp
                    setDisplayHomeAsUpEnabled(isChecked)
                }
            }
            isChecked = true
        }
        homeButtonEnabledCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            (activity as ToolBarTestActivity).supportActionBar?.apply {
                //左上角的图标是否可以点击
                setHomeButtonEnabled(isChecked)
                setDisplayShowHomeEnabled(isChecked)
                // setHomeButtonEnabled和setDisplayShowHomeEnabled共同起作用，
                // 如果setHomeButtonEnabled设成false，
                // 即使setDisplayShowHomeEnabled设成true，图标也不能点击
            }
        }
        //
        navigationCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            // 设置导航图标一定要设置在setsupportactionbar后面才有用
            toolbarView?.apply {
                navigationIcon =
                    if (isChecked) {
                        resources.getDrawable(R.mipmap.ic_actionbar_back)
                    } else {
                        homeAsUpEnabledCheckBox.isChecked = false
                        null
                    }
            }
        }
        //设置APP图标
        logoIconCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            toolbarView?.apply {
                setLogo(
                    if (isChecked)
                        R.mipmap.ic_launcher_round
                    else
                        R.mipmap.avatar_default
                )
            }
        }
        //设置title
        mainTitleCheckBox.isChecked = true
        mainTitleCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            toolbarView?.apply {
                title = if (isChecked) "GreekMyth" else ""
            }
        }
        subTitleCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            toolbarView?.apply {
                title = if (isChecked) "SubTitle" else ""
            }
        }
        collapseIconCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            toolbarView?.apply {
                setCollapseIcon(
                    if (isChecked)
                        R.mipmap.pic_video_default
                    else
                        R.mipmap.avatar_default
                )
            }
        }
        customLayoutCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            toolbarView?.apply {
                findViewById<TextView>(R.id.tvTitle).text =
                    if (isChecked) {
                        "自定义布局"
                    } else {
                        ""
                    }
            }
        }
    }
}