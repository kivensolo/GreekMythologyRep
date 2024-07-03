package com.kingz.four_components.activity

import androidx.annotation.DrawableRes
import com.kingz.customdemo.R
import com.zeke.module_login.SplashActivity


data class AppIcon(
    val id : String,
    val component: String,
    @DrawableRes
    val foregroundResource: Int
)

val appIcons : List<AppIcon> = listOf(
    AppIcon(
        id = "default",
        component = SplashActivity::class.java.name,
        foregroundResource = R.drawable.ic_app_launcher
    ),
    AppIcon(
        id = "red",
        component = "com.zeke.module_login.SplashActivityRed",
        foregroundResource = R.drawable.ic_launcher_background_red
    )
)