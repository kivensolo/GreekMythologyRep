package com.kingz.coroutines.demo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zeke.example.coroutines.R
import kotlinx.android.synthetic.main.activity_first.*

class FirstActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        btMVVM.setOnClickListener {
            startActivity(Intent(this, ChaptersActivity::class.java))
        }
    }

}