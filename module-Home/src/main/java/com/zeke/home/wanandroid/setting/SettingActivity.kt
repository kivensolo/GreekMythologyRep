package com.zeke.home.wanandroid.setting

import android.os.Bundle
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
 */
@Route(path = RouterConfig.PAGE_SETTING)
class SettingActivity : AppBarActivity(), ColorChooserDialog.ColorCallback {

    override fun initData(savedInstanceState: Bundle?) {
        defaultFragmentName = SettingFragment::class.java.name
        pageTitle = resources.getString(R.string.setting)
        super.initData(savedInstanceState)
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
