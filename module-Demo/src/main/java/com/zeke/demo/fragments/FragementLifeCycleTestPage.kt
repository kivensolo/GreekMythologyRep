package com.zeke.demo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.kingz.module.common.base.BaseActivity
import com.zeke.demo.R
import com.zeke.demo.databinding.PageFragemntsLifecycleBinding

/**
 * author: King.Z <br>
 * date:  2021/2/24 19:35 <br>
 * description: Fragement 生命周期演示 <br>
 *   add() & replace():
 *      只有在Fragment数量大于等于2的时候，调用add()还是replace()的区别才能体现出来。
 *      add()连续添加Fragment时，每个Fragment生命周期中的onAttach()-onResume()都会被调用,
 *      replace()来添加Fragment的时候，第2次添加会导致第1个Fragment被销毁,
 *      执行第2个Fragment的onAttach()方法之前会先执行第1个Fragment的onPause()-onDetach()方法.
 *   show() & hide()：
 *      Fragment的生命周期方法并不会被执行，仅仅是Fragment的View被显示或者隐藏。
 *      hide时并没有将view从viewtree中删除,随后可用show()方法将view设置为显示
 *   attach() & detach()：
 *      detach()会将view从viewtree中删除，onPause()-onDestroyView()周期都会被执行。
 *      此时fragment的状态依然保持着,在使用attach()时会再次调用onCreateView()来重绘视图,。
 *      Fixme: 但是这个demo detach不会让View消失。
 *   remove():
 *      完成onPause()-onDetach()周期
 *
 */
class FragementLifeCycleTestPage:BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_fragemnts_lifecycle)
        var viewBind:PageFragemntsLifecycleBinding = PageFragemntsLifecycleBinding.inflate(layoutInflater)
        viewBind.changeA.setOnClickListener {
            changeFragment(this@FragementLifeCycleTestPage.findViewById(R.id.fragment_a))
        }
        viewBind.changeA2.setOnClickListener {
            changeFragment(this@FragementLifeCycleTestPage.findViewById(R.id.fragment_b))
        }
        viewBind.changeA3.setOnClickListener {
            changeFragment(this@FragementLifeCycleTestPage.findViewById(R.id.fragment_c))

        }
    }

    private fun changeFragment(fragment:Fragment) {
        val fm = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()
        if (fragment.isHidden) {
            fragmentTransaction.attach(fragment)
        } else {
            fragmentTransaction.detach(fragment)
        }
        fragmentTransaction.commit()
    }
}