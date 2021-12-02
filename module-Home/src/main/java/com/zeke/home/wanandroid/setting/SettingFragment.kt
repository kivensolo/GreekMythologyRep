package com.zeke.home.wanandroid.setting

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.afollestad.materialdialogs.color.ColorChooserDialog
import com.kingz.module.home.R
import com.zeke.home.wanandroid.setting.SettingCons.Companion.KEY_OF_THEME_COLOR
import com.zeke.home.wanandroid.widget.IconPreference

/**
 * author：ZekeWang
 * date：2021/12/2
 * description：设置页面的Fragment
 */
class SettingFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
        private var context: SettingActivity? = null
    private lateinit var colorPreview: IconPreference

    companion object{
        fun getInstance():SettingFragment{
            return SettingFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        colorPreview = findPreference(KEY_OF_THEME_COLOR) as IconPreference
        setDefaultText()
        context = activity as SettingActivity
        findPreference(KEY_OF_THEME_COLOR).setOnPreferenceClickListener {
            ColorChooserDialog.Builder(context!!, R.string.choose_theme_color)
                .backButton(R.string.back)
                .cancelButton(R.string.cancel)
                .doneButton(R.string.done)
                .customButton(R.string.custom)
                .presetsButton(R.string.back)
                .allowUserColorInputAlpha(false)
                .show()
            false
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        //Inflate the hierarchy
        setPreferencesFromResource(R.xml.pref_setting,rootKey)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
         key ?: return
        if (key == KEY_OF_THEME_COLOR) {
            colorPreview.setView()
        }
    }

    private fun setDefaultText() {
        try {
            findPreference("clearCache").summary = "暂无缓存数据大小"
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}