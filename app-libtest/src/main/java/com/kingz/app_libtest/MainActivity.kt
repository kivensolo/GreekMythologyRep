package com.kingz.app_libtest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.zeke.network.OkHttpClientManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testBtn.setOnClickListener {
            val response  = OkHttpClientManager.getAsyn("http://www.baidu.com")
            Log.d("s",response.toString())
        }
    }
}
