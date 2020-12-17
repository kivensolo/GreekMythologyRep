package com.zeke.ktx.modules.aac.sharedvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kingz.customdemo.R

/**
 * author: King.Z <br>
 * date:  2020/12/17 22:53 <br>
 * description:  <br>
 */
class SharedSecondVMActivity : AppCompatActivity() {
    @VMScope(DEFAULT_SCOPE)
    lateinit var vm:ScopeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectVM()
        "第2个Activity使用的ViewModel为:".log()
        vm.log()
        setContentView(R.layout.activity_simple)
    }
}