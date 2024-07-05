package com.zeke.ktx.modules.aac

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kingz.customdemo.databinding.ActivityCrossfadeBinding
import com.kingz.module.common.BaseActivity
import com.zeke.kangaroo.zlog.ZLog
import com.zeke.ktx.modules.aac.viewmodels.User
import com.zeke.ktx.modules.aac.viewmodels.UserInfoViewModel

/**
 * author: King.Z <br></br>
 * date:  2020/4/20 23:37 <br></br>
 * 一个简单的展示ViewModel和LiveData的
 */
class ViewModelDemoActivity : BaseActivity() {
    private var mShortAnimationDuration = 800
    private var testViewModel: UserInfoViewModel? = null

    private lateinit var viewBinding:ActivityCrossfadeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCrossfadeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.content.visibility = View.GONE
        crossFade()

        // 创建与当前activity相关的LiveData 对象
        testViewModel = ViewModelProviders.of(this).get(UserInfoViewModel::class.java)

        // Create the observer which updates the UI.
        val nameObserver: Observer<User> = Observer {
            // 当数据变化时回调onChange方法
            ZLog.d("onChanged ", "it: $it")
            viewBinding.loremIpsumView?.text = it.toString()
        }

        /**
        * 将 Observer 对象附加到 LiveData 对象， observe() 方法会采用 LifecycleOwner 对象。
        * 这样会使 Observer 对象订阅 LiveData 对象，以使其收到有关更改的通知。
        * 通常情况下，可以在界面控制器（如 Activity 或 Fragment）中附加 Observer 对象。
        *
        * 若要注册未关联 LifecycleOwner 对象可以使用 observeForever(Observer) 方法
        */
        testViewModel?.run{
            testStringLiveData.observe(this@ViewModelDemoActivity, nameObserver)
            // 更新存储在 LiveData 对象中的值时，
            // 它会触发所有已注册的观察者（只要附加的 LifecycleOwner 处于活跃状态)
            testStringLiveData.value = User("KingZ","27","Boy")
        }

        viewBinding.loremIpsumView.setOnClickListener {
            testViewModel?.let {
                it.testStringLiveData.value = User("KingZZZZZ","27777","Boyyyy")
            }
        }
    }

    private fun crossFade() { // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        viewBinding.content.alpha = 0f
        viewBinding.content.visibility = View.VISIBLE
        viewBinding.content.animate()?.apply {
            alpha(1f)
            duration = mShortAnimationDuration.toLong()
            setListener(null)
        }
        viewBinding.loadingSpinner.animate()?.apply {
            alpha(0f)
            duration = mShortAnimationDuration.toLong()
            setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    viewBinding.loadingSpinner.visibility = View.GONE
                }
            })
        }
    }
}