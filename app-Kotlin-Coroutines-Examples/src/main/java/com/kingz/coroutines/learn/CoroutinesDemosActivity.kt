package com.kingz.coroutines.learn

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kingz.coroutines.learn.errorhandling.exceptionhandler.ExceptionHandlerActivity
import com.kingz.coroutines.learn.errorhandling.supervisor.IgnoreErrorAndContinueActivity
import com.kingz.coroutines.learn.retrofit.parallel.ParallelNetworkCallsActivity
import com.kingz.coroutines.learn.retrofit.series.SeriesNetworkCallsActivity
import com.kingz.coroutines.learn.retrofit.single.SingleNetworkCallActivity
import com.kingz.coroutines.learn.room.RoomDBActivity
import com.kingz.coroutines.learn.task.onetask.LongRunningTaskActivity
import com.kingz.coroutines.learn.task.twotasks.TwoLongRunningTasksActivity
import com.kingz.coroutines.learn.timeout.TimeoutActivity
import com.zeke.example.coroutines.R

class CoroutinesDemosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startSingleNetworkCallActivity(view: View) {
        startActivity(Intent(this@CoroutinesDemosActivity,
            SingleNetworkCallActivity::class.java))
    }

    fun startSeriesNetworkCallsActivity(view: View) {
        startActivity(Intent(this@CoroutinesDemosActivity,
            SeriesNetworkCallsActivity::class.java))
    }

    fun startParallelNetworkCallsActivity(view: View) {
        startActivity(Intent(this@CoroutinesDemosActivity,
            ParallelNetworkCallsActivity::class.java))
    }

    fun startRoomDatabaseActivity(view: View) {
        startActivity(Intent(this@CoroutinesDemosActivity,
            RoomDBActivity::class.java))
    }

    fun startTimeoutActivity(view: View) {
        startActivity(Intent(this@CoroutinesDemosActivity,
            TimeoutActivity::class.java))
    }

    fun startExceptionHandlerActivity(view: View) {
        startActivity(Intent(this@CoroutinesDemosActivity,
            ExceptionHandlerActivity::class.java))
    }

    fun startIgnoreErrorAndContinueActivity(view: View) {
        startActivity(Intent(this@CoroutinesDemosActivity,
            IgnoreErrorAndContinueActivity::class.java))
    }

    fun startLongRunningTaskActivity(view: View) {
        startActivity(Intent(this@CoroutinesDemosActivity,
            LongRunningTaskActivity::class.java))
    }

    fun startTwoLongRunningTasksActivity(view: View) {
        startActivity(Intent(this@CoroutinesDemosActivity,
            TwoLongRunningTasksActivity::class.java))
    }

}
