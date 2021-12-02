package com.zeke.home.wanandroid.setting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.afollestad.materialdialogs.color.ColorChooserDialog
import com.alibaba.android.arouter.facade.annotation.Route
import com.kingz.module.common.router.RouterConfig
import com.kingz.module.common.setting.SettingUtil
import com.kingz.module.home.R
import com.kingz.module.wanandroid.activity.AppBarActivity

/**
 * FIXME 存在的问题：
 * 1. Title有padding
 * 2. checkBox未选中时没颜色
 * 2. 颜色圈圈，太大了
 */
@Route(path = RouterConfig.PAGE_SETTING)
class SettingActivity : AppBarActivity(), ColorChooserDialog.ColorCallback {

    private val EXTRA_SHOW_FRAGMENT = "show_fragment"
    private val EXTRA_SHOW_FRAGMENT_ARGUMENTS = "show_fragment_args"
    private val EXTRA_SHOW_FRAGMENT_TITLE = "show_fragment_title"

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun getContentLayout(): Int  = R.layout.activity_setting

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        val initFragment: String = intent.getStringExtra(EXTRA_SHOW_FRAGMENT) ?: ""
        val initArguments: Bundle = intent.getBundleExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS) ?: Bundle()
        val initTitle: String = intent.getStringExtra(EXTRA_SHOW_FRAGMENT_TITLE)
                ?: resources.getString(R.string.setting)

        if (initFragment.isEmpty()) { //Default is SettingFragement
            setupFragment(SettingFragment::class.java.name, initArguments)
        } else {
            setupFragment(initFragment, initArguments)
        }

        findViewById<Toolbar>(R.id.toolbar).run {
            title = initTitle
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupFragment(fragmentName: String, args: Bundle) {
//        val fragment = Fragment.instantiate(this, fragmentName, args)
        val factory = supportFragmentManager.fragmentFactory
        val fragment = factory.instantiate(classLoader, fragmentName)
        fragment.arguments = args
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(R.id.container, fragment)
        transaction.commitAllowingStateLoss()
    }

    private fun onBuildStartFragmentIntent(fragmentName: String, args: Bundle?, title: String?): Intent {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.setClass(this, javaClass)
        intent.putExtra(EXTRA_SHOW_FRAGMENT, fragmentName)
        intent.putExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS, args)
        intent.putExtra(EXTRA_SHOW_FRAGMENT_TITLE, title)
        return intent
    }

    fun startWithFragment(fragmentName: String, args: Bundle?,
                          resultTo: Fragment?, resultRequestCode: Int, title: String?) {
        val intent = onBuildStartFragmentIntent(fragmentName, args, title)
        if (resultTo == null) {
            startActivity(intent)
        } else {
            resultTo.startActivityForResult(intent, resultRequestCode)
        }
    }

    override fun onColorChooserDismissed(dialog: ColorChooserDialog) {
    }

    override fun onColorSelection(dialog: ColorChooserDialog, selectedColor: Int) {
        if (!dialog.isAccentMode) {
            SettingUtil.setColor(selectedColor)
        }
        initColor()
//        EventBus.getDefault().post(ColorEvent(true))
    }
}
