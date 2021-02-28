package com.zeke.demo.meterial

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zeke.demo.R
import kotlinx.android.synthetic.main.activity_appbarlayout_demo.*

/**
 * author：ZekeWang
 * date：2021/2/28
 * description：AppBarLayout的Demo展示
 */
class AppBarLayoutDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appbarlayout_demo)
        show_normal?.setOnClickListener {
            startActivity(Intent(this@AppBarLayoutDemoActivity,
                AppBarNormalActivity::class.java))
        }
        show_closse?.setOnClickListener {
            startActivity(Intent(this@AppBarLayoutDemoActivity,
                CollapseToolbarNoramalActivity::class.java))
        }
    }
}