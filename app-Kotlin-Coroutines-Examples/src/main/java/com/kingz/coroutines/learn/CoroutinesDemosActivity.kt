package com.kingz.coroutines.learn

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kingz.coroutines.learn.retrofit.parallel.ParallelNetworkCallsActivity
import com.kingz.coroutines.learn.timeout.TimeoutActivity
import com.zeke.example.coroutines.R
import com.zeke.kangaroo.zlog.ZLog

class CoroutinesDemosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ZLog.d("onCreate ")
    }

    fun startParallelNetworkCallsActivity(view: View) {
        ZLog.d("startParallelNetworkCallsActivity ")
        startActivity(Intent(this@CoroutinesDemosActivity,
            ParallelNetworkCallsActivity::class.java))
    }

    fun startTimeoutActivity(view: View) {
        ZLog.d("startParallelNetworkCallsActivity",true)
        startActivity(Intent(this@CoroutinesDemosActivity,
            TimeoutActivity::class.java))
    }
}
