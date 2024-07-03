package com.kingz.four_components.activity

import android.content.ComponentName
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.kingz.base.BaseVMActivity
import com.zeke.kangaroo.zlog.ZLog
import com.zeke.reactivehttp.base.BaseReactiveViewModel

/**
 * author: King.Z <br></br>
 * date:  2024/07/03 16:37 <br></br>
 * description: 通过Acticity-Alias属性改变应用图标<br></br>
 * 主要通过packageManager去动态禁用Component来实现。
 */
class ChangeAppIconActivity : BaseVMActivity(), View.OnClickListener {
    private var bt_1: Button? = null

    private var isDefault = false

    override fun getContentView(): View? {
        val rootView = LinearLayout(this)
        rootView.setBackgroundColor(Color.DKGRAY)
        rootView.setPadding(0, 30, 0, 0)
        val lps = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        bt_1 = Button(this)
        bt_1!!.text = "点击切换App图标"
        bt_1!!.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        rootView.layoutParams = lps
        rootView.addView(bt_1)
        bt_1!!.setOnClickListener(this)
        return rootView
    }

    override fun onClick(v: View) {
        ZLog.d("ChangeAppIconActivity", "To change appIcon")
        if (isDefault) {
            setAppIcon("red")
            isDefault = false
        } else {
            setAppIcon("default")
            isDefault = true
        }
    }

    private fun setAppIcon(iconId: String) {
        val icon = appIcons.find { it.id == iconId }
        if(icon == null){
            ZLog.e("ChangeAppIconActivity", "Change failed, id not found: $iconId ")
            return
        }
        ZLog.d("ChangeAppIconActivity", "Changing....")
        val context = baseContext
        val packageManager = context.packageManager

        appIcons.filterNot { it.id == iconId }.forEach {
            //不是目标icon就禁止掉
            ZLog.d("ChangeAppIconActivity", "禁用:${it.component}")
            packageManager.setComponentEnabledSetting(
                ComponentName(context, it.component),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }
        //开启后Launcher应用会延迟接近10s才生效
        ZLog.w("ChangeAppIconActivity", "启用:${icon.component}")
        packageManager.setComponentEnabledSetting(
            ComponentName(context, icon.component),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override val viewModel: BaseReactiveViewModel
        get() = TODO("Not yet implemented")
}
