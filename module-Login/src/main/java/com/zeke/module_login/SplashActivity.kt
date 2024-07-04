package com.zeke.module_login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.RelativeLayout
import android.widget.VideoView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.kingz.base.BaseVMActivity
import com.kingz.database.DatabaseApplication
import com.kingz.database.entity.UserEntity
import com.kingz.module.common.router.Router
import com.kingz.module.common.router.RouterConfig
import com.kingz.module.common.user.UserInfo
import com.kingz.module.wanandroid.bean.Data
import com.kingz.module.wanandroid.bean.UserInfoBean
import com.zeke.kangaroo.zlog.ZLog
import com.zeke.module_login.databinding.SplashActivityBinding
import com.zeke.module_login.viewmodel.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * author: King.Z <br>
 * date:  2020/7/8 19:03 <br>
 * description: 应用启动欢迎 & 登录页面 <br>
 */
@Route(path = RouterConfig.PAGE_LOGIN)
class SplashActivity : BaseVMActivity(), View.OnClickListener {
    private lateinit var binding: SplashActivityBinding

    private var inputType = InputType.NONE

    override fun getContentView(): View {
        val inflater =layoutInflater
        binding = SplashActivityBinding.inflate(inflater)
        return binding.root
    }

//    override fun createViewModel(): LoginViewModel {
//        return ViewModelProvider(this)[LoginViewModel::class.java]
//    }

    // 使用 'by viewModels()' 的Kotlin属性代理的方式
    // 可以学习一下 IUIActionEventObserver 中的getViewModel
//    override val viewModel: LoginViewModel by viewModels {
//        ViewModelFactory.build { LoginViewModel() }
//    }
    override val viewModel: LoginViewModel by getViewModel(LoginViewModel::class.java) {
        // 初始化时就进行监听注册
        loginInfoData.observe(it, Observer { response ->  //UserInfoBean
            ZLog.d("loginInfoData onChanged: $response")
            if (response != null) {
                if (response.errorCode < 0) {
                    showToast("登陆失败:${response.errorMsg}")
                } else {
                    showToast("欢迎登陆!${(response.data as Data).nickname}")
                    saveUserInfo(response)
                    openMainPage()
                }
            }
        })
    }

    override fun initViewModel() {
        super.initViewModel()
        // Observe livedatas in UI-Thread
        viewModel.registerLiveData.observe(this, Observer {
            ZLog.d("registerData onChanged:", "$it")
            val registerData = it?.data
            if (registerData != null) {
                if (registerData.errorCode == 0) {
                    showToast("注册成功!")
                } else {
                    showToast("注册异常:${registerData.errorMsg}")
                }
            }
        })
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .hideBar(BarHide.FLAG_HIDE_BAR)
            .init()
    }

    /**
     * Save user info to local db.
     */
    private fun saveUserInfo(loginData: UserInfoBean) {
        val userEntity = UserEntity().apply {
            val userInfo = loginData.data
            if (userInfo != null) {
                admin = userInfo.admin
                nickname = userInfo.nickname
                username = userInfo.username
                publicName = userInfo.publicName
                token = userInfo.token
                id = userInfo.id
            }
        }


        lifecycleScope.launch {
            DatabaseApplication.getInstance().getUserDao().insert(userEntity)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        checkUserCacheInfo()
    }

    private fun checkUserCacheInfo() {
        lifecycleScope.launch(Dispatchers.IO) {
            val userInfo = UserInfo.getUserInfor()
            if (userInfo != null) {
                withContext(Dispatchers.Main) {
                    binding.buttonLeft.visibility = View.GONE
                    binding.buttonRight.visibility = View.GONE
                    showToast("欢迎回来${userInfo.username}")
                    ZLog.d("initData check userinfo:", "$userInfo")
                    openMainPage()
                }
            }
        }

    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        binding.buttonRight.setOnClickListener(this)
        binding.buttonLeft.setOnClickListener(this)
        binding.loginView.post {
            binding.loginView.apply {
                val delta = top + height
                translationY = (-1 * delta).toFloat()
            }
        }
        playVideo()
        playAppNameAnim()
    }

    private fun playVideo() {
        val videoUrl = "android.resource://$packageName/${R.raw.welcome_video}"
        binding.videoView.apply {
            setVideoPath(videoUrl)
            layoutParams = RelativeLayout.LayoutParams(-1, -1)
            setOnPreparedListener {
                it.isLooping = true
                it.start()
            }
        }
    }

    private fun playAppNameAnim() {
        ObjectAnimator.ofFloat(binding.appName, "alpha", 0f, 1f).apply {
            duration = 4000
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
            start()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (binding.appName.visibility != View.INVISIBLE) {
                        binding.appName.visibility = View.INVISIBLE
                    }
                }
            })
        }
    }

    override fun onClick(v: View?) {
        val deltaY = binding.loginView.top + binding.loginView.height
        when (inputType) {
            InputType.NONE -> {
                if (v == binding.buttonLeft) {
                    inputType = InputType.LOGIN
                    // change text to login-mode
                    updateButtonText(
                        resources.getText(R.string.button_confirm_login),
                        resources.getText(R.string.button_cancel_login)
                    )
                } else if (v == binding.buttonRight) {
                    inputType = InputType.SIGN_UP
                    binding.loginView.getBinding().rePwd.visibility = View.VISIBLE
                    binding.loginView.getBinding().rePwdLine.visibility = View.VISIBLE
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
                    binding.buttonLeft.id -> confirmLogin()
                    binding.buttonRight.id -> onLoginCancel(deltaY)
                }
            }
            InputType.SIGN_UP -> {
                when (v?.id) {
                    binding.buttonLeft.id -> doUserRegister()
                    binding.buttonRight.id -> onRegisterCancel(deltaY)
                }
            }
        }
    }

    /**
     * Rest inputType to None from SIGN_UP
     */
    private fun onRegisterCancel(deltaY: Int) {
        disMissLoginView(deltaY)
        inputType = InputType.NONE
        binding.buttonLeft.setText(R.string.button_login)
        binding.buttonRight.setText(R.string.button_signup)
    }

    /**
     * Rest inputType to None from LOGIN
     */
    private fun onLoginCancel(deltaY: Int) {
        disMissLoginView(deltaY)
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
        if(BuildConfig.DEBUG){
            viewModel.login("kivensolo", "denglu18")
        }else{
            if (TextUtils.isEmpty(binding.loginView.getBinding().loginName.text ?: "") ||
                TextUtils.isEmpty(binding.loginView.getBinding().loginPwd.text ?: "")) {
                ZLog.e("请输入账号和密码")
                showToast(getStringFromRes(R.string.user_info_invalid_tips))
            } else {
                viewModel.login(binding.loginView.getBinding().loginName.text, binding.loginView.getBinding().loginPwd.text)
            }
        }
    }

    private fun updateButtonText(left: CharSequence, right: CharSequence) {
        binding.buttonLeft.text = left
        binding.buttonRight.text = right
    }

    private fun disMissLoginView(delta: Int) {
        ZLog.d("disMissLoginView")
        binding.loginView.animate().apply {
            translationY((-1 * delta).toFloat())
            alpha(0f)
            duration = 500
        }.start()
        binding.loginView.getBinding().rePwd.visibility = View.GONE
        binding.loginView.getBinding().rePwdLine.visibility = View.GONE
    }

    private fun showLoginView() {
        ZLog.d("showLoginView")

        binding.loginView.animate().apply {
            translationY(0f)
            alpha(1f)
            duration = 500
        }.start()
    }

    // TODO 密码强度判断
    private fun doUserRegister() {
        ZLog.d("Click to register.")
        if (TextUtils.isEmpty(binding.loginView.getBinding().loginName?.text)) {
            showToast("请输入账户名")
            return
        } else if (TextUtils.isEmpty(binding.loginView.getBinding().loginPwd?.text) || binding.loginView.getBinding().loginPwd.text.length <= 7) {
            showToast("请输入有效密码(长度大于7)")
            return
        } else if (TextUtils.isEmpty(binding.loginView.getBinding().rePwd?.text)) {
            showToast("请确认注册密码")
            return
        } else if (!TextUtils.equals(binding.loginView.getBinding().rePwd?.text, binding.loginView.getBinding().loginPwd?.text)) {
            showToast("请输入相同密码")
            return
        }
        viewModel.singUp(
            binding.loginView.getBinding().loginName?.text.toString(),
            binding.loginView.getBinding().loginPwd?.text.toString(),
            binding.loginView.getBinding().loginPwd?.text.toString()
        )
    }

    private fun openMainPage() {
        GlobalScope.launch {
            delay(3000)
            Router.startActivity(RouterConfig.PAGE_MAIN)
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        (binding.videoView as VideoView).pause()
    }

    override fun onResume() {
        super.onResume()
        if (!(binding.videoView as VideoView).isPlaying) {
            (binding.videoView as VideoView).start()
        }
    }

    override fun onDestroy() {
        (binding.videoView as VideoView).stopPlayback()
        super.onDestroy()
    }

    enum class InputType {
        NONE, LOGIN, SIGN_UP
    }
}