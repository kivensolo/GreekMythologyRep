package com.kingz.coroutines.learn

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kingz.coroutines.learn.retrofit.parallel.ParallelNetworkCallsActivity
import com.kingz.coroutines.learn.timeout.TimeoutActivity
import com.zeke.example.coroutines.R

class CoroutinesDemosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startParallelNetworkCallsActivity(view: View) {
        startActivity(Intent(this@CoroutinesDemosActivity,
            ParallelNetworkCallsActivity::class.java))
    }

    fun startTimeoutActivity(view: View) {
        startActivity(Intent(this@CoroutinesDemosActivity,
            TimeoutActivity::class.java))
    }

}
