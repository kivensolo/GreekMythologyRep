package com.zeke.module_login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.VideoView
import androidx.lifecycle.ViewModelProvider
import com.kingz.base.BaseVMActivity
import com.zeke.module_login.repository.LoginRepository
import com.zeke.module_login.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.splash_activity.*

/**
 * author: King.Z <br>
 * date:  2016/9/29 19:03 <br>
 * description: 应用启动欢迎 & 登录页面 <br>
 */
class SplashActivityKt : BaseVMActivity<LoginRepository, LoginViewModel>(), View.OnClickListener {

    private var inputType = InputType.NONE

    override fun getContentView() = R.layout.splash_activity

    override fun createViewModel(): LoginViewModel {
        //TODO ViewModel的创建  统一处理
        return ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun initData(savedInstanceState: Bundle?) {}

    override fun initView(savedInstanceState: Bundle?) {
        if (!BuildConfig.SPLSH_DEBUG) {
            openMainPage()
        } else {
            buttonRight.setOnClickListener(this)
            buttonLeft.setOnClickListener(this)
            loginView.post {
                loginView.apply {
                    val delta = top + height
                    translationY = (-1 * delta).toFloat()
                }
            }
            playVideo()
            playAnim()
        }
    }

    private fun playVideo() {
        val videoUrl = "android.resource://$packageName/${R.raw.welcome_video}"
        if (videoView is VideoView) {
            (videoView as VideoView).setVideoPath(videoUrl)
            (videoView as VideoView).layoutParams = RelativeLayout.LayoutParams(-1, -1)
            (videoView as VideoView).setOnPreparedListener {
                it.apply {
                    isLooping = true
                    start()
                }
            }
        }
    }

    private fun playAnim() {
        ObjectAnimator.ofFloat(appName, "alpha", 0f, 1f).apply {
            duration = 4000
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
            start()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (appName.visibility != View.INVISIBLE) {
                        appName.visibility = View.INVISIBLE
                    }
                }
            })
        }
    }

    override fun onClick(v: View?) {
        val delta = loginView.top + loginView.height
        when (inputType) {
            InputType.NONE -> {
                //回到原来的位置 translationY  相对位置
                loginView.animate().translationY(0f).alpha(1f)
                    .setDuration(500).start()
                if (v == buttonLeft) {
                    inputType = InputType.LOGIN
                    buttonLeft.setText(R.string.button_confirm_login)
                    buttonLeft.setText(R.string.button_cancel_login)
                } else if (v == buttonRight) {
                    inputType = InputType.SIGN_UP;
                    buttonLeft.setText(R.string.button_confirm_signup)
                    buttonRight.setText(R.string.button_cancel_signup)
                }
            }
            InputType.LOGIN -> {
                //TODO 进行登录操作
                loginView.animate().translationY((-1 * delta).toFloat())
                    .alpha(0f).setDuration(500).start()
                inputType = InputType.NONE
                buttonLeft.setText(R.string.button_login)
                buttonRight.setText(R.string.button_signup)
            }
            InputType.SIGN_UP -> {
                //TODO 进行注册操作
                loginView.animate().translationY((-1 * delta).toFloat())
                    .alpha(0f).setDuration(500).start()
                inputType = InputType.NONE
                buttonLeft.setText(R.string.button_login)
                buttonRight.setText(R.string.button_signup)
            }
        }
    }

    private fun openMainPage() {
        val intent = with(intent) {
            action = "com.kingz.home.mainpage"
            addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        (videoView as VideoView).stopPlayback()
    }

    enum class InputType {
        NONE, LOGIN, SIGN_UP
    }

}