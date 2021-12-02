package com.zeke.home.wanandroid.widget

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.kingz.module.common.setting.SettingUtil
import com.kingz.module.home.R
import com.module.views.img.SmartImageView

class IconPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs) {

    private var circleImageView: SmartImageView? = null

    init {
        widgetLayoutResource = R.layout.item_icon_preference_preview
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        val color = SettingUtil.getAppThemeColor()
        circleImageView = holder.findViewById(R.id.iv_preview) as SmartImageView?
        circleImageView?.setColorFilter(color)
    }

    fun setView() {
        val color = SettingUtil.getAppThemeColor()
        circleImageView?.setColorFilter(color)
    }
}