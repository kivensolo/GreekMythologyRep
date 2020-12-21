package com.zeke.hlscache

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * 启动单路流页面
     */
    fun onSingleClick(view: View) {
        startActivity(Intent(this, SingleActivity::class.java))
    }

    /**
     * 启动多码率流页面
     */
    fun onMultiClick(view: View) {
        startActivity(Intent(this, MultiActivity::class.java))
    }
}