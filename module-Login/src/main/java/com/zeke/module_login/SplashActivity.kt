package com.zeke.module_login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.RelativeLayout
import android.widget.VideoView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.kingz.base.BaseVMActivity
import com.kingz.base.factory.ViewModelFactory
import com.zeke.kangaroo.utils.ToastUtils
import com.zeke.kangaroo.utils.ZLog
import com.zeke.module_login.entity.Data
import com.zeke.module_login.repository.LoginRepository
import com.zeke.module_login.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.form_view.*
import kotlinx.android.synthetic.main.splash_activity.*

/**
 * author: King.Z <br>
 * date:  2020/7/8 19:03 <br>
 * description: 应用启动欢迎 & 登录页面 <br>
 */
class SplashActivity : BaseVMActivity<LoginRepository, LoginViewModel>(), View.OnClickListener {

    private var inputType = InputType.NONE

    override fun getContentView() = R.layout.splash_activity

//    override fun createViewModel(): LoginViewModel {
//        return ViewModelProvider(this)[LoginViewModel::class.java]
//    }

    // 使用 'by viewModels()' 的Kotlin属性代理的方式
    override val viewModel: LoginViewModel by viewModels {
        ViewModelFactory.build { LoginViewModel() }
    }

    override fun initViewModel() {
        super.initViewModel()
        // Observe livedatas in UI-Thread
        viewModel.loginInfoData.observe(this@SplashActivity, Observer {
            ZLog.d("loginInfoData onChanged:", "$it")
            val loginData = it
            if(loginData != null){
                if (loginData.errorCode < 0) {
                    ToastUtils.show(this, "登陆失败:${loginData.errorMsg}")
                } else {
                    ToastUtils.show(this, "欢迎登陆!${(loginData.data as Data).nickname}")
                    // TODO 进行数据库数据存储 和跳转
                    //openMainPage()
                }
            }
        })

        viewModel.registerData.observe(this, Observer {
            ZLog.d("registerData onChanged:", "$it")
            val registerData = it?.body()
            if (registerData != null) {
                if (registerData.errorCode < 0) {
                    ToastUtils.show(this, "注册异常:${registerData.errorMsg}")
                } else {
                    ToastUtils.show(this, "注册成功!")
                    //TODO 进行默认用户登录操作
                }
            }
        })
    }

    override fun initData(savedInstanceState: Bundle?) {}

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        buttonRight.setOnClickListener(this)
        buttonLeft.setOnClickListener(this)
        loginView.post {
            loginView.apply {
                val delta = top + height
                translationY = (-1 * delta).toFloat()
            }
        }
        playVideo()
        playAppNameAnim()
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

    private fun playAppNameAnim() {
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
        val deltaY = loginView.top + loginView.height
        when (inputType) {
            InputType.NONE -> {
                if (v == buttonLeft) {
                    inputType = InputType.LOGIN
                    // change text to login-mode
                    updateButtonText(
                        resources.getText(R.string.button_confirm_login),
                        resources.getText(R.string.button_cancel_login)
                    )
                } else if (v == buttonRight) {
                    inputType = InputType.SIGN_UP
                    re_pwd.visibility = View.VISIBLE
                    // change text to singup-mode
                    updateButtonText(
                        resources.getText(R.string.button_confirm_signup),
                        resources.getText(R.string.button_cancel_signup)
                    )
                }
                showLoginView()
            }
            InputType.LOGIN -> {
                when (v?.id) {
                    buttonLeft.id -> confirmLogin()
                    buttonRight.id -> onLoginCancel(deltaY)
                }
            }
            InputType.SIGN_UP -> {
                when (v?.id) {
                    buttonLeft.id -> doUserRegister()
                    buttonRight.id -> onRegisterCancel(deltaY)
                }
            }
        }
    }

    /**
     * Rest inputType to None from SIGN_UP
     */
    private fun onRegisterCancel(deltaY: Int) {
        dissmissLoginView(deltaY)
        inputType = InputType.NONE
        buttonLeft.setText(R.string.button_login)
        buttonRight.setText(R.string.button_signup)
    }

    /**
     * Rest inputType to None from LOGIN
     */
    private fun onLoginCancel(deltaY: Int) {
        dissmissLoginView(deltaY)
        inputType = InputType.NONE
        updateButtonText(
            resources.getText(R.string.button_login),
            resources.getText(R.string.button_signup)
        )
    }

    /**
     * Do login logic.
     */
    private fun confirmLogin() {
        ZLog.d("click to login.")
        if (TextUtils.isEmpty(login_name?.text ?: "") ||
            TextUtils.isEmpty(login_pwd?.text ?: "")
        ) {
            ZLog.e("请输入账号和密码")
        } else {
            viewModel.login(login_name!!.text, login_pwd!!.text)
        }
    }

    private fun updateButtonText(left: CharSequence, right: CharSequence) {
        buttonLeft?.text = left
        buttonRight?.text = right
    }

    private fun dissmissLoginView(delta: Int) {
        ZLog.d("dissmissLoginView")
        loginView.animate().apply {
            translationY((-1 * delta).toFloat())
            alpha(0f)
            duration = 500
        }.start()
        re_pwd.visibility = View.GONE
        re_pwd_line.visibility = View.GONE
    }

    private fun showLoginView() {
        ZLog.d("showLoginView")
        loginView.animate().apply {
            translationY(0f)
            alpha(1f)
            duration = 500
        }.start()
    }

    // TODO 密码强度判断
    private fun doUserRegister() {
        ZLog.d("Click to register.")
        if (TextUtils.isEmpty(login_name?.text)) {
            ToastUtils.show(this, "请输入账户名")
            return
        } else if (TextUtils.isEmpty(login_pwd?.text) || login_pwd.text.length <= 7) {
            ToastUtils.show(this, "请输入有效密码(长度大于7)")
            return
        } else if (TextUtils.isEmpty(re_pwd?.text)) {
            ToastUtils.show(this, "请确认注册密码")
            return
        } else if (!TextUtils.equals(re_pwd?.text, login_pwd?.text)) {
            ToastUtils.show(this, "请输入相同密码")
            return
        }
        viewModel.singUp(
            login_name?.text.toString(),
            login_pwd?.text.toString(),
            login_pwd?.text.toString()
        )
    }

    private fun openMainPage() {
        val intent = with(intent) {
            action = "com.kingz.home.mainpage"
            addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }
        startActivity(intent)
        finish()
    }

    override fun onPause() {
        super.onPause()
        (videoView as VideoView).pause()
    }

    override fun onResume() {
        super.onResume()
        if (!(videoView as VideoView).isPlaying) {
            (videoView as VideoView).start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (videoView as VideoView).stopPlayback()
    }

    enum class InputType {
        NONE, LOGIN, SIGN_UP
    }
}