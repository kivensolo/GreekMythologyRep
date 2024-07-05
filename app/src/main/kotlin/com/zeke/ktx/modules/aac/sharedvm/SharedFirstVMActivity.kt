package com.zeke.ktx.modules.aac.sharedvm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.kingz.customdemo.R

/**
 * author: King.Z <br>
 * date:  2020/12/17 22:53 <br>
 * description:  <br>
 */
class SharedFirstVMActivity : AppCompatActivity() {
    @VMScope(DEFAULT_SCOPE)
    lateinit var vm: ScopeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectVM()
        "第1个Activity使用的ViewModel为:".log()
        vm.log()

        setContentView(R.layout.activity_vmshared_first)
        var openSecondBtn = findViewById<Button>(R.id.open_second_page)
        openSecondBtn.setOnClickListener {
            startActivity(Intent(this,
                SharedSecondVMActivity::class.java))
        }
    }
}